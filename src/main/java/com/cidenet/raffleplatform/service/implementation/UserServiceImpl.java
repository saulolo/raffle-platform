package com.cidenet.raffleplatform.service.implementation;

import com.cidenet.raffleplatform.dto.AuthCreateUserRequestDTO;
import com.cidenet.raffleplatform.dto.AuthLoginRequestDTO;
import com.cidenet.raffleplatform.dto.AuthResponseDTO;
import com.cidenet.raffleplatform.enums.RoleEnum;
import com.cidenet.raffleplatform.model.Role;
import com.cidenet.raffleplatform.model.User;
import com.cidenet.raffleplatform.repository.RoleRepository;
import com.cidenet.raffleplatform.repository.UserRepository;
import com.cidenet.raffleplatform.utils.JwtUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementa los métodos de la interfaz UserDetailsService
 */
@Service
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    /**
     * Permite cargar un usuario por su nombre de usuario
     * @param documentNumber Nombre de usuario
     * @return Objeto de tipo UserDetails que contiene la información del usuario
     * @throws UsernameNotFoundException Excepción que se lanza si no encuentra el
     *                                   usuario
     */
    @Override
    public UserDetails loadUserByUsername(String documentNumber) throws UsernameNotFoundException {

        try {
            User user = userRepository.findByDocumentNumber(documentNumber)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "El usuario con número de documento: " + documentNumber + " no existe."));

            List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

            user.getRoles().forEach(
                    role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

            user.getRoles().stream().flatMap(role -> role.getPermissionList().stream())
                    .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

            return new org.springframework.security.core.userdetails.User(user.getDocumentNumber(), user.getPassword(),
                    user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                    user.isAccountNonLocked(), authorityList);

        } catch (Exception e) {
            throw new UsernameNotFoundException("Error al cargar usuario con número de documento " + documentNumber, e);
        }
    }

    /**
     * Permite autenticar un usuario
     * @param authLoginRequestDTO Objeto de tipo AuthLoginRequest que contiene la
     *                            información del usuario
     * @return Objeto de tipo AuthResponse que contiene la información de la
     * autenticación
     * @throws BadCredentialsException Excepción que se lanza si las credenciales
     *                                 son inválidas
     */
    public AuthResponseDTO loginUser(AuthLoginRequestDTO authLoginRequestDTO) {
        try {
            String documentNumber = authLoginRequestDTO.documentNumber();
            String password = authLoginRequestDTO.password();

            Authentication authentication = this.authenticate(documentNumber, password);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtUtils.createToken(authentication);

            AuthResponseDTO authResponseDTO = new AuthResponseDTO(documentNumber,
                    "El usuario inició sesión exitosamente", accessToken, true);
            return authResponseDTO;
        } catch (BadCredentialsException e) {
            return new AuthResponseDTO("Error", "Número de documento o contraseña no válidos", null, false);
        } catch (Exception e) {
            return new AuthResponseDTO("Error", "ocurrió un error inesperado", null, false);

        }
    }

    /**
     * Permite autenticar un usuario
     * @param documentNumber Nombre de usuario
     * @param password       Contraseña
     * @return Objeto de tipo Authentication que contiene la información de la
     * autenticación
     * @throws BadCredentialsException Excepción que se lanza si las credenciales
     *                                 son inválidas
     */
    public Authentication authenticate(String documentNumber, String password) {
        UserDetails userDetails = this.loadUserByUsername(documentNumber);

        if (userDetails == null) {
            throw new BadCredentialsException("Número de documento o contraseña no válidos");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Contraseña invalida");
        }

        return new UsernamePasswordAuthenticationToken(documentNumber, userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    /**
     * Permite crear un usuario
     * @param authCreateUser Objeto de tipo AuthCreateUserRequest que contiene la
     *                       información del usuario
     * @return Objeto de tipo AuthResponse que contiene la información de la
     * creación del usuario
     */
    public AuthResponseDTO createUser(AuthCreateUserRequestDTO authCreateUser) {

        Set<String> roleRequestList = new HashSet<>(authCreateUser.roleRequest().roles());
        Set<Role> roles = new HashSet<>();
        String documentNumber;

        if (Boolean.FALSE.equals(userRepository.existsByDocumentNumber(authCreateUser.documentNumber()))) {
            documentNumber = authCreateUser.documentNumber();
        } else {
            throw new IllegalArgumentException("El número de documento ya existe");
        }

        for (String roleRequest : roleRequestList) {
            try {
                RoleEnum roleEnum = RoleEnum.valueOf(roleRequest.toUpperCase());
                Role role = roleRepository.findByRoleEnum(roleEnum)
                        .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + roleEnum));
                roles.add(role);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Rol no válido: " + roleRequest);
            }
        }

        User user = User.builder().username(authCreateUser.username())
                .password(passwordEncoder.encode(authCreateUser.password())).email(authCreateUser.email())
                .documentType(authCreateUser.documentType()).documentNumber(documentNumber)
                .cellPhone(authCreateUser.cellPhone()).fullName(authCreateUser.fullName()).isEnabled(true)
                .isAccountNonExpired(true).isAccountNonLocked(true).isCredentialsNonExpired(true).roles(roles).build();
        userRepository.save(user);
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();

        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name()));
            role.getPermissionList().forEach(permission -> {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            });
        });

        Authentication authentication = new UsernamePasswordAuthenticationToken(documentNumber,
                authCreateUser.password(), authorities);

        String accessToken = jwtUtils.createToken(authentication);
        return new AuthResponseDTO(documentNumber, "Usuario creado exitosamente", accessToken, true);
    }
}
