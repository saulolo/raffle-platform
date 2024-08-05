package com.cidenet.raffleplatform.controller;

import com.cidenet.raffleplatform.dto.AuthCreateUserRequestDTO;
import com.cidenet.raffleplatform.dto.AuthLoginRequestDTO;
import com.cidenet.raffleplatform.dto.AuthResponseDTO;
import com.cidenet.raffleplatform.service.implementation.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contiene las operaciones relacionadas con los usuarios
 */
@RestController
@RequestMapping("/v1/user")
@Tag(name = "User", description = "Operaciones relacionadas con los usuarios")
public class UserController {

    private UserServiceImpl userServiceImlp;

    public UserController(UserServiceImpl userServiceImlp) {
        this.userServiceImlp = userServiceImlp;
    }

    /**
     * Registra un usuario
     * 
     * @param authCreateUserRequestDTO
     * @return AuthResponse con el usuario creado y el token de autenticación
     *         generado.
     */
    @PostMapping("/signup")
    @Operation(summary = "Crea un usuario", description = "Crear un nuevo usuario en el sistema")
    public ResponseEntity<AuthResponseDTO> register(
            @RequestBody @Valid AuthCreateUserRequestDTO authCreateUserRequestDTO) {
        return new ResponseEntity<>(userServiceImlp.createUser(authCreateUserRequestDTO), HttpStatus.CREATED);
    }

    /**
     * Inicia sesión
     * 
     * @param userRequest
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Iniciar sesión en el sistema")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthLoginRequestDTO userRequest) {
        return new ResponseEntity<>(userServiceImlp.loginUser(userRequest), HttpStatus.OK);
    }
}
