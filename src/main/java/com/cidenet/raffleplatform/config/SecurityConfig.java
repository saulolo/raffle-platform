package com.cidenet.raffleplatform.config;

import com.cidenet.raffleplatform.config.filter.JwtTokenValidator;
import com.cidenet.raffleplatform.service.implementation.UserServiceImpl;
import com.cidenet.raffleplatform.utils.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuración de seguridad de la aplicación.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    private final JwtUtils jwtUtils;

    public SecurityConfig(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    /**
     * Crea y devuelve un SecurityFilterChain que se encarga de la configuración de
     * seguridad de la aplicación.
     * @param httpSecurity la configuración de seguridad utilizada para crear el
     *                     SecurityFilterChain.
     * @return un SecurityFilterChain creado a partir de la configuración de
     * seguridad proporcionada.
     * @throws Exception si ocurre un error durante la creación del
     *                   SecurityFilterChain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable()).httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {

                    // Configuring public endpoints
                    http.requestMatchers(HttpMethod.GET, "/v3/api-docs/**")
                            .permitAll();
                    http.requestMatchers(HttpMethod.GET, "/swagger-ui/**")
                            .permitAll();

                    http.requestMatchers(HttpMethod.GET, "/v1/method/get")
                            .permitAll();

                    http.requestMatchers(HttpMethod.POST, "/v1/user/**").
                            permitAll();

                    // Configuring private endpoints
                    http.requestMatchers(HttpMethod.POST, "/v1/auth/post")
                            .hasAnyRole("ADMIN", "VOLUNTEER");
                    http.requestMatchers(HttpMethod.DELETE, "/v1/auth/delete")
                            .hasAnyAuthority("DELETE", "CREATE");

                    http.requestMatchers(HttpMethod.POST, "/v1/raffle/create")
                            .hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.PUT, "/v1/raffle/{id}")
                            .hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.GET, "/v1/raffle/all")
                            .hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.GET, "/v1/raffle/{id}")
                            .hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.DELETE, "/v1/raffle/{id}")
                            .hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.PUT, "/v1/raffle/inactivate/{id}")
                            .hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.POST, "/v1/raffle/status")
                            .hasAnyRole("ADMIN", "VOLUNTEER");

                    http.requestMatchers(HttpMethod.POST, "/v1/ticket/create")
                            .hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.PUT, "/v1/ticket/update")
                            .hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.GET, "/v1/ticket/tickets/{id}")
                            .hasAnyRole("ADMIN", "VOLUNTEER");


                    // Configuration of the rest of the endpoints (NOT SPECIFIED)
                    http.anyRequest().denyAll();
                }).addFilterBefore(new JwtTokenValidator(jwtUtils),
                        BasicAuthenticationFilter.class).build();
    }


    /**
     * Crea y devuelve un AuthenticationManager utilizando la configuración de
     * autenticación proporcionada.
     * @param authenticationConfiguration la configuración de autenticación
     *                                    utilizada para crear el
     *                                    AuthenticationManager.
     * @return un AuthenticationManager creado a partir de la configuración de
     * autenticación proporcionada.
     * @throws Exception si ocurre un error durante la creación del
     *                   AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Crea y devuelve un AuthenticationProvider utilizando el servicio de usuario
     * proporcionado.
     * @param userService el servicio de usuario utilizado para crear el
     *                    AuthenticationProvider.
     * @return un AuthenticationProvider creado a partir del servicio de usuario
     * proporcionado.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserServiceImpl userService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }

    /**
     * Crea y devuelve un UserDetailsService utilizando una lista vacía de
     * UserDetails.
     * @return un UserDetailsService creado a partir de una lista vacía de
     * UserDetails.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        List<UserDetails> userDetailsList = new ArrayList<>();
        return new InMemoryUserDetailsManager(userDetailsList);
    }

    /**
     * Crea y devuelve un PasswordEncoder que no encripta las contraseñas.
     * @return un PasswordEncoder que no encripta las contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
