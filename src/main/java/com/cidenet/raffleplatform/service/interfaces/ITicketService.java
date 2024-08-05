package com.cidenet.raffleplatform.service.interfaces;

import com.cidenet.raffleplatform.dto.TicketRaffleResponseDTO;
import com.cidenet.raffleplatform.model.Ticket;

import java.util.List;

/**
 * Define los métodos a implementar en el servicio de boletos.
 */
public interface ITicketService {

    /**
     * Crea una lista de boletos para un sorteo.
     * @param raffleId        Identificador del sorteo.
     * @param numberOfFigures Número de cifras de los boletos.
     * @return Lista de boletos creados.
     */
    List<Ticket> createTicket(Long raffleId, int numberOfFigures);

    /**
     * Obtiene la cantidad de boletos disponibles para una rifa.
     * @param raffleId Identificador de la rifa.
     * @return Cantidad de boletos disponibles.
     */
    Integer getCountAvailableTickets(Long raffleId);

    /**
     * Actualiza los boletos de una rifa.
     * @param raffleId        Identificador de la rifa.
     * @param numberOfFigures Número de cifras de los boletos.
     */
    void updateTickets(Long raffleId, int numberOfFigures);

    /**
     * Obtener los boletos de una rifa.
     * @param raffleId Identificador de la rifa.
     * @return Lista de boletos.
     */
    List<TicketRaffleResponseDTO> getTicketsByRaffleId(Long raffleId) throws Exception;
}
