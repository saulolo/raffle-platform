package com.cidenet.raffleplatform.dto;

import com.cidenet.raffleplatform.enums.RaffleStatusEnum;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Representa la información de una rifa
 */
@Builder
@JsonPropertyOrder({"id", "name", "prizeDescription", "prizeValue", "raffleDate", "createdByFullName", "description",
        "status", "availableTickets", "reservedTickets", "paidTickets", "ticketPrice"})
public record RaffleResponseDTO(Long id,
                                String name,
                                String prizeDescription,
                                Integer prizeValue,
                                LocalDateTime raffleDate,
                                String createdByFullName,
                                String description,
                                RaffleStatusEnum status,
                                Long availableTickets,
                                Long reservedTickets,
                                Long paidTickets,
                                Integer ticketPrice) {
}
