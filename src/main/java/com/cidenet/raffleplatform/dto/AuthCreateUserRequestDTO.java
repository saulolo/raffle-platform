package com.cidenet.raffleplatform.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

/**
 * DTO que representa la petición para crear un usuario.
 */
@Validated
public record AuthCreateUserRequestDTO(@NotBlank String username,
                                       @NotBlank String password,
                                       String cellPhone,
                                       String email,
                                       String fullName,
                                       String documentType,
                                       String documentNumber,
                                       @Valid AuthCreateRoleRequestDTO roleRequest) {
}
