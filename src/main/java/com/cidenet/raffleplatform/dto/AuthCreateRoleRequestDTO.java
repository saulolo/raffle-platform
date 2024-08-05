package com.cidenet.raffleplatform.dto;

import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Rrepresenta la petición para crear un rol.
 */
@Validated
public record AuthCreateRoleRequestDTO(
        @Size(max = 2, message = "El nombre de usuario debe tener como máximo 2 caracteres.")
        List<String> roles) {
}
