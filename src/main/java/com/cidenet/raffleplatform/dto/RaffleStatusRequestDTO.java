package com.cidenet.raffleplatform.dto;

import com.cidenet.raffleplatform.enums.RaffleStatusEnum;
import jakarta.validation.constraints.NotNull;

/**
 * Representa la información necesaria para cambiar el estado de un sorteo.
 */
public record RaffleStatusRequestDTO(@NotNull RaffleStatusEnum raffleStatus) {
}
