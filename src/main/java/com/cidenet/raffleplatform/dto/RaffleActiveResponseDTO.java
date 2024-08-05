package com.cidenet.raffleplatform.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Representa la información de una rifa
 */
@Builder
@JsonPropertyOrder({"id", "name", "raffleDate", "availableTickets", "reservedTickets", "paidTickets", "ticketPrice"})
public record RaffleActiveResponseDTO(Long id,
                                      String name,
                                      LocalDateTime raffleDate,
                                      Long availableTickets,
                                      Long reservedTickets,
                                      Long paidTickets,
                                      Integer ticketPrice) {
}
