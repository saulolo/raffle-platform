package com.cidenet.raffleplatform.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Permite la creación y validación de tokens JWT
 */
@Component
public class JwtUtils {

    private static final int EXPIRATION_TIME =  28800000;

    @Value("${security.jwt.key.private}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    /**
     * Crea un token JWT con los datos del usuario autenticado
     *
     * @param authentication Datos de autenticación del usuario
     * @return Token JWT
     */
    public String createToken(Authentication authentication) {

        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

        String username = authentication.getPrincipal().toString();

        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String jwtToken = JWT.create().withIssuer(this.userGenerator).withSubject(username)
                .withClaim("authorities", authorities).withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withJWTId(UUID.randomUUID().toString()).withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);

        return jwtToken;
    }

    /**
     * Valida un token JWT
     *
     * @param token Token JWT
     * @return DecodedJWT
     */
    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(this.privateKey);

            JWTVerifier verifier = JWT.require(algoritmo).withIssuer(this.userGenerator).build();

            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Token inválido, no autorizado");
        }
    }

    /**
     * Extrae el nombre de usuario del token JWT
     *
     * @param decodedJWT Token JWT decodificado
     * @return Nombre de usuario
     */
    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    /**
     * Extrae los roles del token JWT
     *
     * @param decodedJWT Token JWT decodificado
     * @return Roles
     */
    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

    /**
     * Extrae todos los claims del token JWT
     *
     * @param decodedJWT Token JWT decodificado
     * @return Claims
     */
    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims();
    }
}
