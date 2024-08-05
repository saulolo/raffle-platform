package com.cidenet.raffleplatform.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

/**
 * Clase de configuración Swagger.
 * Contiene la definición de OpenAPI y el esquema de seguridad para la API.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "API de la plataforma de rifa HF",
                description = "API para gestión de rifas",
                termsOfService = "https://cidenet.com.co/pod/",
                version = "1.0.0",
                summary = "Una plataforma para gestionar y realizar rifas",
                contact = @Contact(
                        name = "Cidenet",
                        url = "https://cidenet.com.co/contactanos/",
                        email = "comunicaciones@cidenet.com.co"
                ),
                license = @License(
                        name = "propiedad",
                        url = "https://cidenet.com.co/"
                )
        ),
        servers = {
                @Server(
                        url = "/raffle/api/"
                )
        },
        security = @SecurityRequirement(
                name = "Token de seguridad"
        )
)
@SecurityScheme(
        name = "Token de seguridad",
        description = "Token de acceso para la API",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
