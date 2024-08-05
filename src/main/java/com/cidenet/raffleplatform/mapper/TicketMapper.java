package com.cidenet.raffleplatform.mapper;

import com.cidenet.raffleplatform.dto.TicketRaffleResponseDTO;
import com.cidenet.raffleplatform.dto.TicketResponseDTO;
import com.cidenet.raffleplatform.model.Ticket;

/**
 * Mapea un ticket a un DTO de respuesta
 */
public final class TicketMapper {

    private TicketMapper() {
    }


    /**
     * Mapea un ticket a un DTO de respuesta
     * @param ticket ticket a mapear
     * @return DTO de respuesta
     */
    public static TicketResponseDTO toTicketResponseDTO(Ticket ticket) {
        return TicketResponseDTO.builder().
                id(ticket.getId()).
                ticketNumber(ticket.getTicketNumber())
                .build();
    }


    /**
     * Mapea un ticket a un DTO de respuesta de rifa
     * @param ticket ticket a mapear
     * @return DTO de respuesta de rifa
     */
    public static TicketRaffleResponseDTO toTicketRaffleResponseDTO(Ticket ticket) {
        return TicketRaffleResponseDTO.builder()
                .id(ticket.getId())
                .ticketNumber(ticket.getTicketNumber())
                .status(ticket.getStatus())
                .name(ticket.getRaffle().getName())
                .prizeValue(ticket.getRaffle().getPrizeValue())
                .raffleDate(ticket.getRaffle().getRaffleDate())
                .ticketPrice(ticket.getRaffle().getTicketPrice())
                .build();
    }
}
