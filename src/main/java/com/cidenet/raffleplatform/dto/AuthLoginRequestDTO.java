package com.cidenet.raffleplatform.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para la autenticación de un usuario
 */
public record AuthLoginRequestDTO(@NotBlank String documentNumber,
                                  @NotBlank String password) {
}
