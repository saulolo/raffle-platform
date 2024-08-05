package com.cidenet.raffleplatform.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * DTO para la respuesta de autenticación.
 */
@JsonPropertyOrder({ "username", "message", "jwt", "status" })
public record AuthResponseDTO(String username,
                              String message,
                              String jwt,
                              boolean status) {
}
