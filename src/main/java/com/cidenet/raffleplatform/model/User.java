package com.cidenet.raffleplatform.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Representa la entidad User en la base de datos
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre de usuario es obligatorio")
    @NotEmpty(message = "El nombre de usuario no puede estar vacío")
    @Size(max = 50, message = "El nombre de usuario no puede tener más de 50 caracteres")
    @Column(name = "username", unique = true)
    private String username;

    @NotNull(message = "El nombre completo es obligatorio")
    @NotEmpty(message = "El nombre completo no puede estar vacío")
    @Size(max = 100, message = "El nombre completo no puede tener más de 100 caracteres")
    @Column(name = "full_name")
    private String fullName;

    @NotNull(message = "El tipo de documento es obligatorio")
    @NotEmpty(message = "El tipo de documento no puede estar vacío")
    @Column(name = "document_type", nullable = false)
    private String documentType;

    @NotNull(message = "El número de documento es obligatorio")
    @NotEmpty(message = "El número de documento no puede estar vacío")
    @Size(max = 20, message = "El número de documento no puede tener más de 20 caracteres")
    @Column(name = "document_number")
    private String documentNumber;

    @Email(message = "El email no es válido")
    @Size(max = 50, message = "El email no puede tener más de 50 caracteres")
    @NotNull(message = "El email es obligatorio")
    @NotEmpty(message = "El email no puede estar vacío")
    @Column(name = "email")
    private String email;

    @NotNull(message = "El número de celular es obligatorio")
    @NotEmpty(message = "El número de celular no puede estar vacío")
    @Size(min = 10, max = 10, message = "El número de celular debe tener exactamente 10 dígitos")
    @Column(name = "cell_phone")
    private String cellPhone;

    @NotNull(message = "La contraseña es obligatoria")
    @NotEmpty(message = "La contraseña no puede estar vacía")
    @Column(name = "password")
    private String password;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "is_account_non_expired")
    private boolean isAccountNonExpired;

    @Column(name = "is_account_non_locked")
    private boolean isAccountNonLocked;

    @Column(name = "is_credentials_non_expired")
    private boolean isCredentialsNonExpired;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Raffle> raffles;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name = "fk_user_id_users_roles")),
            inverseJoinColumns = @JoinColumn(name = "role_id",
                    foreignKey = @ForeignKey(name = "fk_role_id_users_roles")))
    private Set<Role> roles = new HashSet<>();
}
