package com.cidenet.raffleplatform.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa la información necesaria para crear una boleta.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TicketCreateRequestDTO {

    @NotNull
    private Long raffleId;

    @NotNull
    private int numberOfFigures;
}
