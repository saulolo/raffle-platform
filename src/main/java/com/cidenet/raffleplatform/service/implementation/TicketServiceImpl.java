package com.cidenet.raffleplatform.service.implementation;

import com.cidenet.raffleplatform.dto.TicketRaffleResponseDTO;
import com.cidenet.raffleplatform.enums.TicketStatusEnum;
import com.cidenet.raffleplatform.mapper.TicketMapper;
import com.cidenet.raffleplatform.model.Raffle;
import com.cidenet.raffleplatform.model.Ticket;
import com.cidenet.raffleplatform.repository.RaffleRepository;
import com.cidenet.raffleplatform.repository.TicketRepository;
import com.cidenet.raffleplatform.service.interfaces.ITicketService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementa los métodos de la interfaz ITicketService
 */
@Service
public class TicketServiceImpl implements ITicketService {

    private static final int BASE_10 = 10;
    private final TicketRepository ticketRepository;
    private final RaffleRepository raffleRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, RaffleRepository raffleRepository) {
        this.ticketRepository = ticketRepository;
        this.raffleRepository = raffleRepository;
    }

    /**
     * Permite crear boletos para una rifa
     * @param raffleId        Identificador de la rifa
     * @param numberOfFigures Número de cifras que tendrá el boleto
     * @return Lista de boletos creados
     * @throws RuntimeException Excepción que se lanza si ocurre un error al crear
     *                          los boletos
     */
    @Override
    @Transactional
    public List<Ticket> createTicket(Long raffleId, int numberOfFigures) {
        try {
            Raffle raffle = raffleRepository.findById(raffleId)
                    .orElseThrow(() -> new RuntimeException("Rifa no encontrada con id: " + raffleId));

            int totalTickets = (int) Math.pow(BASE_10, numberOfFigures);
            List<Ticket> tickets = new ArrayList<>();

            for (int i = 1; i <= totalTickets; i++) {
                Ticket ticket = Ticket.builder().ticketNumber(i).status(TicketStatusEnum.AVAILABLE).raffle(raffle)
                        .build();
                tickets.add(ticket);
            }
            return ticketRepository.saveAll(tickets);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear una boleta", e);
        }
    }

    /**
     * Obtiene la cantidad de boletos disponibles para una rifa
     * @param raffleId Identificador de la rifa
     * @return Cantidad de boletos disponibles
     */
    @Override
    @Transactional(readOnly = true)
    public Integer getCountAvailableTickets(Long raffleId) {
        return ticketRepository.countAvailableTicketsByRaffleId(raffleId);
    }

    /**
     * Actualiza los boletos de una rifa
     * @param raffleId        Identificador de la rifa
     * @param numberOfFigures Número de cifras que tendrá el boleto
     * @throws RuntimeException Excepción que se lanza si ocurre un error al actualizar
     *                          los boletos
     */
    @Override
    @Transactional
    public void updateTickets(Long raffleId, int numberOfFigures) {

        try {
            raffleRepository.findById(raffleId)
                    .orElseThrow(() -> new RuntimeException("Rifa no encontrada con id: " + raffleId));

            int totalTickets = (int) Math.pow(BASE_10, numberOfFigures) - 1;

            int availableTickets = getCountAvailableTickets(raffleId);

            if (availableTickets != totalTickets) {

                final int batchSize = 1000;
                for (int i = 0; i < totalTickets; i += batchSize) {
                    ticketRepository.deleteByRaffleIdInBatch(raffleId, i, batchSize);
                }

                for (int i = 0; i < totalTickets; i += batchSize) {
                    createTicketBatch(raffleId, numberOfFigures, i, batchSize);
                }
            } else {
                throw new RuntimeException(
                        "El número de boletas disponibles es diferente del número total de boletas.");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al actualizar boletas", e);
        }
    }

    /**
     * Crea boletos en lotes
     * @param raffleId        Identificador de la rifa
     * @param numberOfFigures Número de cifras que tendrá el boleto
     * @param start           Número de boleto inicial
     * @param batchSize       Tamaño del lote
     * @throws RuntimeException Excepción que se lanza si ocurre un error al crear
     *                          los boletos
     */
    @Transactional
    public void createTicketBatch(Long raffleId, int numberOfFigures, int start, int batchSize) {
        try {
            Raffle raffle = raffleRepository.findById(raffleId)
                    .orElseThrow(() -> new RuntimeException("Rifa no encontrada con id: " + raffleId));

            int totalTickets = Math.min((int) Math.pow(BASE_10, numberOfFigures), start + batchSize);
            List<Ticket> tickets = new ArrayList<>();

            for (int i = start; i <= totalTickets; i++) {
                Ticket ticket = Ticket.builder().ticketNumber(i).status(TicketStatusEnum.AVAILABLE).raffle(raffle)
                        .build();
                tickets.add(ticket);
            }
            ticketRepository.saveAll(tickets);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear una boleta", e);
        }
    }

    /**
     * Elimina boletos en lotes
     * @param raffleId  Identificador de la rifa
     * @param start     Número de boleto inicial
     * @param batchSize Tamaño del lote
     * @throws RuntimeException Excepción que se lanza si ocurre un error al eliminar
     *                          los boletos
     */
    @Transactional
    public void deleteByRaffleIdInBatch(Long raffleId, int start, int batchSize) {
        try {
            List<Ticket> tickets = ticketRepository.findTicketsByRaffleIdInRange(raffleId, start, batchSize);
            ticketRepository.deleteAll(tickets);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar boletos en lotes", e);
        }
    }

    /**
     * Obtiene los boletos de una rifa
     * @param raffleId Identificador de la rifa
     * @return Lista de boletos
     * @throws RuntimeException Excepción que se lanza si ocurre un error al obtener
     *                          los boletos
     */
    @Override
    @Transactional(readOnly = true)
    public List<TicketRaffleResponseDTO> getTicketsByRaffleId(Long raffleId) {
        try {
            if (raffleId == null || raffleId <= 0) {
                throw new RuntimeException("Id ingresado no válido" + raffleId);
            }
            List<Ticket> tickets = ticketRepository.findByRaffleId(raffleId);
            return tickets.stream()
                    .map(TicketMapper::toTicketRaffleResponseDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error:" + e.getMessage(), e);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al acceder a los datos de las boletas", e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado", e);
        }
    }
}
