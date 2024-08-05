package com.cidenet.raffleplatform.service.interfaces;

import com.cidenet.raffleplatform.dto.RaffleActiveResponseDTO;
import com.cidenet.raffleplatform.dto.RaffleResponseDTO;
import com.cidenet.raffleplatform.enums.RaffleStatusEnum;
import com.cidenet.raffleplatform.model.Raffle;

import java.util.List;
import java.util.Optional;

/**
 * Define los métodos a implementar en el servicio de sorteos.
 */
public interface IRaffleService {

    /**
     * Crea una rifa.
     * @param raffle Rifa a crear.
     */
    Raffle createRaffle(Raffle raffle);

    /**
     * Obtiene todas las rifas.
     * @throws Exception
     */
    List<RaffleResponseDTO> getAllRaffles() throws Exception;

    /**
     * Obtiene una rifa por su identificador.
     * @param id Identificador de la rifa.
     */
    Optional<Raffle> getRaffleById(Long id);

    /**
     * Actualiza una rifa.
     * @param id     Identificador de la rifa.
     * @param raffle Rifa a actualizar.
     */
    Raffle updateRaffle(Long id, Raffle raffle);

    /**
     * Elimina una rifa.
     * @param id Identificador de la rifa.
     */
    void logicalDeleteRaffle(Long id);


    /**
     * Inactiva una rifa.
     * @param id Identificador de la rifa.
     */
    void inactivateRaffle(Long id);


    /**
     * Obtiene todas las rifas segun las boletos disponibles, reservados y pagados, ordenadas por estado y fecha de
     * rifa.
     * @param raffleStatus estado de la rifa
     * @return lista de rifas con las boletos disponibles, reservados y pagados
     */
    List<RaffleActiveResponseDTO> getRafflesByStatusAndFutureRaffleDate(RaffleStatusEnum raffleStatus);
}
