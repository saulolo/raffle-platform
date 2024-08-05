package com.cidenet.raffleplatform.model;

import com.cidenet.raffleplatform.enums.TicketStatusEnum;
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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Representa una boleta en la base de datos.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_number", updatable = false)
    private Integer ticketNumber;

    @Column(name = "reservation_date")
    private LocalDateTime reservationDate;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @NotNull(message = "El estado de la boleta es requerido")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TicketStatusEnum status;

    @Size(max = 200)
    @Column(name = "comments")
    private String comments;

    @Lob
    @Column(name = "payment_file", columnDefinition = "OID")
    private byte[] paymentFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "fk_customer_id_tickets"))
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raffle_id", foreignKey = @ForeignKey(name = "fk_raffle_id_tickets"))
    private Raffle raffle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_id_tickets"))
    private User user;
}
