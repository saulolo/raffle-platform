package com.cidenet.raffleplatform.dto;

import com.cidenet.raffleplatform.enums.TicketStatusEnum;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Representa una boleta en la base de datos.
 */
@Builder
@JsonPropertyOrder({ "id", "ticketNumber", "status", "name", "prizeValue", "raffleDate", "ticketPrice" })
public record TicketRaffleResponseDTO(Long id,
                                      Integer ticketNumber,
                                      TicketStatusEnum status,
                                      String name,
                                      Integer prizeValue,
                                      LocalDateTime raffleDate,
                                      Integer ticketPrice) {
}
