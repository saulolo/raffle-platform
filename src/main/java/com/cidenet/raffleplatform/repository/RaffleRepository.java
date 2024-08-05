package com.cidenet.raffleplatform.repository;

import com.cidenet.raffleplatform.enums.RaffleStatusEnum;
import com.cidenet.raffleplatform.model.Raffle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Se encarga de gestionar la persistencia de los datos de las rifas.
 */
@Repository
public interface RaffleRepository extends JpaRepository<Raffle, Long> {


    /**
     * Obtiene todas las rifas con la cantidad de boletos disponibles, reservados y pagados, ordenadas por estado y
     * fecha de rifa.
     * @param activeStatus   estado activo de la rifa
     * @param inactiveStatus estado inactivo de la rifa
     * @return lista de arreglos de objetos con la información de las rifas
     */
    @Query("SELECT r.id, r.name, r.prizeDescription, r.prizeValue, r.raffleDate, "
            + "r.createdBy.fullName, r.description, r.status, "
            + "(SELECT COUNT(t) FROM Ticket t WHERE t.raffle = r AND t.status = 'AVAILABLE') AS availableTickets, "
            + "(SELECT COUNT(t) FROM Ticket t WHERE t.raffle = r AND t.status = 'RESERVED') AS reservedTickets, "
            + "(SELECT COUNT(t) FROM Ticket t WHERE t.raffle = r AND t.status = 'PAID') AS paidTickets, "
            + "r.ticketPrice "
            + "FROM Raffle r WHERE r.status IN (:activeStatus, :inactiveStatus) "
            + "ORDER BY CASE WHEN r.status = :activeStatus THEN 1 ELSE 2 END, r.raffleDate")
    List<Object[]> findAllRafflesWithTicketCountsAndOrdered(@Param("activeStatus") RaffleStatusEnum activeStatus,
                                                            @Param("inactiveStatus") RaffleStatusEnum inactiveStatus);


    /**
     * Obtiene todas las rifas con la cantidad de boletos disponibles, reservados y pagados, ordenadas por estado y
     * fecha de rifa.
     * @param status
     * @return
     */
    @Query("SELECT r FROM Raffle r WHERE r.status = :status AND r.raffleDate > CURRENT_DATE ORDER BY r.raffleDate ASC")
    List<Raffle> findAllByStatusAndFutureRaffleDate(@Param("status") RaffleStatusEnum status);
}
