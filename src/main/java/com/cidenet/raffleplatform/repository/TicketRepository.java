package com.cidenet.raffleplatform.repository;

import com.cidenet.raffleplatform.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Se encarga de gestionar la persistencia de los datos de los boletos.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Cuenta la cantidad de boletos disponibles para una rifa.
     * @param raffleId Identificador de la rifa.
     * @return Cantidad de boletos disponibles.
     */
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.raffle.id = :raffleId AND t.status = 'AVAILABLE'")
    int countAvailableTicketsByRaffleId(@Param("raffleId") Long raffleId);

    /**
     * Elimina los boletos de una rifa.
     * @param raffleId Identificador de la rifa.
     */
    void deleteByRaffleId(Long raffleId);


    /**
     * Obtiene los boletos de una rifa en un rango específico.
     * @param raffleId Identificador de la rifa.
     * @param start    Inicio del rango.
     * @param end      Fin del rango.
     * @return Lista de boletos.
     */
    @Query("SELECT t FROM Ticket t WHERE t.raffle.id = :raffleId AND t.ticketNumber BETWEEN :start AND :end")
    List<Ticket> findTicketsByRaffleIdInRange(@Param("raffleId") Long raffleId, @Param("start") int start,
                                              @Param("end") int end);


    /**
     * Elimina los boletos de una rifa en un rango específico.
     * @param raffleId Identificador de la rifa.
     * @param start    Inicio del rango.
     * @param end      Fin del rango.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Ticket t WHERE t.raffle.id = :raffleId AND t.ticketNumber BETWEEN :start AND :end")
    void deleteByRaffleIdInBatch(@Param("raffleId") Long raffleId, @Param("start") int start, @Param("end") int end);


    /**
     * Obtiene los boletos de una rifa.
     * @param raffleId Identificador de la rifa.
     * @return Lista de boletos.
     */
    List<Ticket> findByRaffleId(Long raffleId);
}
