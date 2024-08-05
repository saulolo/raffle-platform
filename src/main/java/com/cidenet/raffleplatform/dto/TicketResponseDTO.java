package com.cidenet.raffleplatform.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

/**
 * Representa la información de un tiquete
 */
@Builder
@JsonPropertyOrder({ "id", "ticketNumber", "status", "raffle" })
public record TicketResponseDTO(Long id,
                                Integer ticketNumber) {
}
