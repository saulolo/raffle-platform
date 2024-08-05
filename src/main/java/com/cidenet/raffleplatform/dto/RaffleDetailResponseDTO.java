package com.cidenet.raffleplatform.dto;

import com.cidenet.raffleplatform.enums.RaffleStatusEnum;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Representa la información detallada de una rifa
 */
@Builder
@JsonPropertyOrder({ "id", "name", "prizeDescription", "isDonatedPrize", "prizeValue", "creationDate", "raffleDate",
        "raffleSelection", "numberOfFigures", "ticketPrice", "winnerNumber", "status", "description", "image",
        "createdByFullName" })
public record RaffleDetailResponseDTO(Long id,
                                      String name,
                                      String prizeDescription,
                                      Boolean isDonatedPrize,
                                      Integer prizeValue,
                                      LocalDateTime creationDate,
                                      LocalDateTime raffleDate,
                                      String raffleSelection,
                                      Integer numberOfFigures,
                                      Integer ticketPrice,
                                      Integer winnerNumber,
                                      RaffleStatusEnum status,
                                      String description,
                                      String image,
                                      String createdByFullName) {
}
