package com.cidenet.raffleplatform.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

import java.util.List;

/**
 * Representa la entidad Customer en la base de datos
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @NotNull(message = "El nombre completo es obligatorio")
    @NotEmpty(message = "El nombre completo no puede estar vacío")
    @Column(name = "full_name")
    private String fullName;

    @NotNull(message = "El número de celular es obligatorio")
    @NotEmpty(message = "El número de celular no puede estar vacío")
    @Size(min = 10, max = 10, message = "El número de celular debe tener exactamente 10 dígitos")
    private String cellPhone;

    @Email(message = "El email no es válido")
    @Size(max = 50, message = "El email no puede tener más de 50 caracteres")
    @NotNull(message = "El email es obligatorio")
    @NotEmpty(message = "El email no puede estar vacío")
    private String email;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ticket> tickets;
}
