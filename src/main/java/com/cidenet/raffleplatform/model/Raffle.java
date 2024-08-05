package com.cidenet.raffleplatform.model;

import com.cidenet.raffleplatform.enums.RaffleStatusEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Representa un sorteo en la base de datos.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "raffles")
public class Raffle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100, message = "El nombre de la rifa no puede tener más de 100 caracteres")
    @NotNull(message = "El nombre de la rifa es obligatorio")
    @NotEmpty(message = "El nombre de la rifa no puede estar vacío")
    @Column(name = "name")
    private String name;

    @Size(max = 200, message = "La descripción de la rifa no puede tener más de 200 caracteres")
    @NotNull(message = "La descripción de la rifa es obligatoria")
    @NotEmpty(message = "La descripción de la rifa no puede estar vacía")
    @Column(name = "prize_description")
    private String prizeDescription;

    @Column(name = "is_donated_prize")
    private Boolean isDonatedPrize;

    @PositiveOrZero(message = "El valor del premio debe ser mayor o igual a 0")
    @Column(name = "prize_value")
    private Integer prizeValue;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Future(message = "La fecha del sorteo debe de ser superior a la actual")
    @NotNull(message = "La fecha del sorteo es obligatoria")
    @Column(name = "raffle_date")
    private LocalDateTime raffleDate;

    @Size(min = 1, max = 1)
    @Pattern(regexp = "L|S", flags = Pattern.Flag.CASE_INSENSITIVE, message = "La selección de la rifa debe ser 'L' "
            + "de loteria o 'S' de sorteo")
    @Column(name = "raffle_selection")
    private String raffleSelection;

    @Min(value = 1, message = "El número de cifras debe ser mayor a 0")
    @Max(value = 3, message = "El número de cifras debe ser menor a 4")
    @NotNull(message = "El número de cifras es obligatorio")
    @Column(name = "number_of_figures")
    private Integer numberOfFigures;

    @PositiveOrZero(message = "El precio del ticket debe ser mayor o igual a 0")
    @NotNull(message = "El precio del ticket es obligatorio")
    @Column(name = "ticket_price")
    private Integer ticketPrice;

    @PositiveOrZero(message = "El número de ganadores debe ser mayor o igual a 0")
    @Column(name = "winner_number")
    private Integer winnerNumber;

    @NotNull(message = "El estado de la rifa es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RaffleStatusEnum status;

    @Size(max = 200, message = "La descripción de la rifa no puede tener más de 200 caracteres")
    @NotNull(message = "La descripción de la rifa es obligatoria")
    @NotEmpty(message = "La descripción de la rifa no puede estar vacía")
    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_created_by_raffles"))
    private User createdBy;

    @OneToMany(mappedBy = "raffle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ticket> tickets;
}
