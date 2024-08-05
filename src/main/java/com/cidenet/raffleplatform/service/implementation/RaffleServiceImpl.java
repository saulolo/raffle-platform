package com.cidenet.raffleplatform.service.implementation;

import com.cidenet.raffleplatform.dto.RaffleActiveResponseDTO;
import com.cidenet.raffleplatform.dto.RaffleResponseDTO;
import com.cidenet.raffleplatform.enums.RaffleStatusEnum;
import com.cidenet.raffleplatform.mapper.RaffleMapper;
import com.cidenet.raffleplatform.model.Raffle;
import com.cidenet.raffleplatform.model.User;
import com.cidenet.raffleplatform.repository.RaffleRepository;
import com.cidenet.raffleplatform.repository.TicketRepository;
import com.cidenet.raffleplatform.repository.UserRepository;
import com.cidenet.raffleplatform.service.interfaces.IRaffleService;
import com.cidenet.raffleplatform.service.interfaces.ITicketService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Clase que implementa los métodos de la interfaz IRaffleService
 */
@Service
public class RaffleServiceImpl implements IRaffleService {

    private final RaffleRepository raffleRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final ITicketService iTicketService;

    public RaffleServiceImpl(RaffleRepository raffleRepository, UserRepository userRepository,
                             TicketRepository ticketRepository, ITicketService iTicketService) {
        this.raffleRepository = raffleRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.iTicketService = iTicketService;
    }


    /**
     * Permite crear un sorteo
     * @param raffleRequest Objeto de tipo Raffle que contiene la información del
     *                      sorteo
     * @return Objeto de tipo Raffle que contiene la información del sorteo creado
     * @throws RuntimeException Excepción que se lanza si ocurre un error al crear
     *                          el sorteo
     */
    @Override
    @Transactional
    public Raffle createRaffle(Raffle raffleRequest) {

        String documentNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado "
                        + "con número de documento: " + documentNumber));

        if (raffleRequest.getRaffleDate() != null && raffleRequest
                .getRaffleDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de la rifa no puede ser anterior a la actual.");
        }

        Raffle raffle = Raffle.builder().name(raffleRequest.getName())
                .prizeDescription(raffleRequest.getPrizeDescription())
                .isDonatedPrize(raffleRequest.getIsDonatedPrize())
                .prizeValue(raffleRequest.getPrizeValue())
                .creationDate(LocalDateTime.now())
                .raffleDate(raffleRequest.getRaffleDate()
                        != null ? raffleRequest.getRaffleDate() : LocalDateTime.now())
                .raffleSelection(raffleRequest.getRaffleSelection())
                .numberOfFigures(raffleRequest.getNumberOfFigures())
                .ticketPrice(raffleRequest.getTicketPrice())
                .winnerNumber(raffleRequest.getWinnerNumber())
                .status(raffleRequest.getStatus())
                .description(raffleRequest.getDescription())
                .image(raffleRequest.getImage())
                .createdBy(user).build();
        Raffle savedRaffle = raffleRepository.save(raffle);

        iTicketService.createTicket(savedRaffle.getId(), savedRaffle.getNumberOfFigures());

        return savedRaffle;
    }


    /**
     * Permite obtener todas las rifas
     * @return Lista de objetos de tipo RaffleResponseDTO que contienen la
     * información de las rifas
     * @throws Exception Excepción que se lanza si ocurre un error al obtener las
     *                   rifas
     */
    @Override
    @Transactional(readOnly = true)
    public List<RaffleResponseDTO> getAllRaffles() throws Exception {
        try {
            List<Object[]> results = raffleRepository.findAllRafflesWithTicketCountsAndOrdered(RaffleStatusEnum.ACTIVE,
                    RaffleStatusEnum.INACTIVE);
            return results.stream()
                    .map(RaffleMapper::toRaffleResponseDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException dae) {
            throw new Exception("Error al acceder a los datos de la rifa", dae);
        } catch (Exception e) {
            throw new Exception("Error al obtener las rifas", e);
        }
    }


    /**
     * Permite obtener un sorteo por su identificador
     * @param id Identificador del sorteo
     * @return Objeto de tipo Raffle que contiene la información del sorteo
     * @throws RuntimeException         Excepción que se lanza si ocurre un error al
     *                                  obtener el sorteo
     * @throws DataAccessException      Excepción que se lanza si ocurre un error al
     *                                  acceder a los datos del sorteo
     * @throws IllegalArgumentException Excepción que se lanza si el identificador
     *                                  es nulo o menor o igual a cero
     * @throws Exception                Excepción que se lanza si ocurre un error
     *                                  inesperado
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Raffle> getRaffleById(Long id) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("ID ingresado no válida: " + id);
            }
            return raffleRepository.findById(id);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error:" + e.getMessage(), e);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al acceder a los datos de la rifa", e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado", e);
        }
    }


    /**
     * Permite actualizar una rifa
     * @param id            Identificador de la rifa
     * @param raffleRequest Objeto de tipo Raffle que contiene la información de la
     *                      rifa a actualizar
     * @return Objeto de tipo Raffle que contiene la información de la rifa
     * actualizado
     * @throws RuntimeException         Excepción que se lanza si ocurre un error al
     *                                  actualizar la rifa
     * @throws DataAccessException      Excepción que se lanza si ocurre un error al
     *                                  acceder a los datos de la rifa
     * @throws IllegalArgumentException Excepción que se lanza si el identificador
     *                                  es nulo o menor o igual a cero
     * @throws Exception                Excepción que se lanza si ocurre un error
     *                                  inesperado
     */
    @Override
    @Transactional
    public Raffle updateRaffle(Long id, Raffle raffleRequest) {
        try {
            Optional<Raffle> existingRaffleOptional = raffleRepository.findById(id);
            if (existingRaffleOptional.isPresent()) {
                Raffle existingRaffle = existingRaffleOptional.get();

                if (raffleRequest.getRaffleDate() != null
                        && raffleRequest.getRaffleDate().isBefore(LocalDateTime.now())) {
                    throw new IllegalArgumentException("La fecha de la rifa no puede ser anterior a la actual.");
                }

                Raffle raffleToUpdate = Raffle.builder().id(existingRaffle.getId())
                        .creationDate(existingRaffle.getCreationDate())
                        .name(raffleRequest.getName() != null ? raffleRequest.getName() : existingRaffle.getName())
                        .prizeDescription(
                                raffleRequest.getPrizeDescription() != null ? raffleRequest.getPrizeDescription()
                                        : existingRaffle.getPrizeDescription())
                        .isDonatedPrize(raffleRequest.getIsDonatedPrize() != null ? raffleRequest.getIsDonatedPrize()
                                : existingRaffle.getIsDonatedPrize())
                        .prizeValue(raffleRequest.getPrizeValue() != null ? raffleRequest.getPrizeValue()
                                : existingRaffle.getPrizeValue())
                        .raffleDate(raffleRequest.getRaffleDate() != null ? raffleRequest.getRaffleDate()
                                : existingRaffle.getRaffleDate())
                        .raffleSelection(raffleRequest.getRaffleSelection() != null ? raffleRequest.getRaffleSelection()
                                : existingRaffle.getRaffleSelection())
                        .numberOfFigures(raffleRequest.getNumberOfFigures() != null ? raffleRequest.getNumberOfFigures()
                                : existingRaffle.getNumberOfFigures())
                        .ticketPrice(raffleRequest.getTicketPrice() != null ? raffleRequest.getTicketPrice()
                                : existingRaffle.getTicketPrice())
                        .winnerNumber(raffleRequest.getWinnerNumber() != null ? raffleRequest.getWinnerNumber()
                                : existingRaffle.getWinnerNumber())
                        .status(raffleRequest.getStatus() != null ? raffleRequest.getStatus()
                                : existingRaffle.getStatus())
                        .description(raffleRequest.getDescription() != null ? raffleRequest.getDescription()
                                : existingRaffle.getDescription())
                        .image(raffleRequest.getImage() != null ? raffleRequest.getImage() : existingRaffle.getImage())
                        .createdBy(existingRaffle.getCreatedBy()).build();
                return raffleRepository.save(raffleToUpdate);
            } else {
                throw new IllegalArgumentException("Rifa no encontrada con ID: " + id);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al actualizar la solicitud de la rifa", e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado", e);
        }
    }


    /**
     * Permite eliminar lógicamente una rifa
     * @param id Identificador de la rifa
     * @throws RuntimeException         Si ocurre un error al eliminar la rifa
     * @throws DataAccessException      Si ocurre un error al acceder a los datos de
     *                                  la rifa
     * @throws IllegalArgumentException Si el identificador es nulo o menor o igual
     *                                  a cero
     * @throws Exception                Si ocurre un error inesperado
     */
    @Override
    @Transactional
    public void logicalDeleteRaffle(Long id) {
        try {
            Raffle existingRaffle = raffleRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Rifa no encontrada con ID: " + id));

            if (existingRaffle.getStatus() == RaffleStatusEnum.ACTIVE
                    || existingRaffle.getStatus() == RaffleStatusEnum.INACTIVE) {

                ticketRepository.deleteByRaffleId(id);

                existingRaffle.setStatus(RaffleStatusEnum.DELETED);
                raffleRepository.save(existingRaffle);
            } else {
                throw new IllegalArgumentException(
                        "La rifa no se puede eliminar porque no está en estado activo o inactivo.");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al eliminar la rifa", e);
        }
    }


    /**
     * Permite inactivar una rifa
     * @param id Identificador de la rifa
     * @throws RuntimeException         Si ocurre un error al inactivar la rifa
     * @throws DataAccessException      Si ocurre un error al acceder a los datos de
     *                                  la rifa
     * @throws IllegalArgumentException Si el identificador es nulo o menor o igual
     *                                  a cero
     * @throws Exception                Si ocurre un error inesperado
     */
    @Override
    @Transactional
    public void inactivateRaffle(Long id) {
        try {
            Optional<Raffle> raffleOptional = raffleRepository.findById(id);

            if (raffleOptional.isPresent()) {
                Raffle raffle = raffleOptional.get();
                raffle.setStatus(RaffleStatusEnum.INACTIVE);
                raffleRepository.save(raffle);
            } else {
                throw new IllegalArgumentException("Rifa no encontrada con ID: " + id);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al inactivar la rifa", e);
        }
    }


    /**
     * Permite obtener todas las rifas segun las boletos disponibles, reservados y pagados, ordenadas por estado y
     * fecha de rifa.
     * @param raffleStatus estado de la rifa
     * @return lista de rifas con las boletos disponibles, reservados y pagados
     * @throws RuntimeException si ocurre un error al obtener las rifas
     */
    @Override
    @Transactional(readOnly = true)
    public List<RaffleActiveResponseDTO> getRafflesByStatusAndFutureRaffleDate(RaffleStatusEnum raffleStatus) {
        try {
            List<Raffle> raffles = raffleRepository.findAllByStatusAndFutureRaffleDate(raffleStatus);
            return raffles.stream()
                    .map(RaffleMapper::toRaffleActiveResponseDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al acceder a los datos de la rifa", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las rifas", e);
        }
    }
}
