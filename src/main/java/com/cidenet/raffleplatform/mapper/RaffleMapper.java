package com.cidenet.raffleplatform.mapper;

import com.cidenet.raffleplatform.dto.RaffleActiveResponseDTO;
import com.cidenet.raffleplatform.dto.RaffleDetailResponseDTO;
import com.cidenet.raffleplatform.dto.RaffleResponseDTO;
import com.cidenet.raffleplatform.enums.RaffleStatusEnum;
import com.cidenet.raffleplatform.enums.TicketStatusEnum;
import com.cidenet.raffleplatform.model.Raffle;
import com.cidenet.raffleplatform.utils.Constants;

import java.time.LocalDateTime;

/**
 * Mapea una rifa a un DTO de respuesta
 */
public final class RaffleMapper {

    private RaffleMapper() {
    }

    public static RaffleResponseDTO toRaffleResponseDTO(Object[] result) {
        return RaffleResponseDTO.builder()
                .id((Long) result[Constants.ZERO_VALUE])
                .name((String) result[Constants.ONE_VALUE])
                .prizeDescription((String) result[Constants.TWO_VALUE])
                .prizeValue((Integer) result[Constants.THREE_VALUE])
                .raffleDate((LocalDateTime) result[Constants.FOUR_VALUE])
                .createdByFullName((String) result[Constants.FIVE_VALUE])
                .description((String) result[Constants.SIX_VALUE])
                .status((RaffleStatusEnum) result[Constants.SEVEN_VALUE])
                .availableTickets((Long) result[Constants.EIGHT_VALUE])
                .reservedTickets((Long) result[Constants.NINE_VALUE])
                .paidTickets((Long) result[Constants.TEN_VALUE])
                .ticketPrice((Integer) result[Constants.ELEVEN_VALUE])
                .build();
    }


    /**
     * Mapea una rifa a un DTO de respuesta detallada
     * @param raffle Rifa a mapear
     * @return DTO de respuesta detallada de la rifa
     */
    public static RaffleDetailResponseDTO toRaffleDetailResponseDTO(Raffle raffle) {
        return RaffleDetailResponseDTO.builder()
                .id(raffle.getId())
                .name(raffle.getName())
                .prizeDescription(raffle.getPrizeDescription())
                .isDonatedPrize(raffle.getIsDonatedPrize())
                .prizeValue(raffle.getPrizeValue())
                .creationDate(raffle.getCreationDate())
                .raffleDate(raffle.getRaffleDate())
                .raffleSelection(raffle.getRaffleSelection())
                .numberOfFigures(raffle.getNumberOfFigures())
                .ticketPrice(raffle.getTicketPrice())
                .winnerNumber(raffle.getWinnerNumber())
                .status(raffle.getStatus())
                .description(raffle.getDescription())
                .image(raffle.getImage())
                .createdByFullName(raffle.getCreatedBy() != null ? raffle.getCreatedBy().getFullName() : "Desconocido")
                .build();
    }


    /**
     * Mapea una rifa a un DTO de respuesta activa
     * @param raffle Rifa a mapear
     * @return DTO de respuesta activa de la rifa
     */
    public static RaffleActiveResponseDTO toRaffleActiveResponseDTO(Raffle raffle) {
        return RaffleActiveResponseDTO.builder()
                .id(raffle.getId())
                .name(raffle.getName())
                .raffleDate(raffle.getRaffleDate())
                .availableTickets(getAvailableTickets(raffle))
                .reservedTickets(getReservedTickets(raffle))
                .paidTickets(getPaidTickets(raffle))
                .ticketPrice(raffle.getTicketPrice())
                .build();
    }


    /**
     * Obtiene la cantidad de boletos disponibles de una rifa
     * @param raffle Rifa a obtener la cantidad de boletos disponibles
     * @return Cantidad de boletos disponibles
     */
    private static Long getAvailableTickets(Raffle raffle) {
        return raffle.getTickets().stream()
                .filter(ticket -> ticket.getStatus() == TicketStatusEnum.AVAILABLE)
                .count();
    }


    /**
     * Obtiene la cantidad de boletos reservados de una rifa
     * @param raffle Rifa a obtener la cantidad de boletos reservados
     * @return Cantidad de boletos reservados
     */
    private static Long getReservedTickets(Raffle raffle) {
        return raffle.getTickets().stream()
                .filter(ticket -> ticket.getStatus() == TicketStatusEnum.RESERVED)
                .count();
    }


    /**
     * Obtiene la cantidad de boletos pagados de una rifa
     * @param raffle Rifa a obtener la cantidad de boletos pagados
     * @return Cantidad de boletos pagados
     */
    private static Long getPaidTickets(Raffle raffle) {
        return raffle.getTickets().stream()
                .filter(ticket -> ticket.getStatus() == TicketStatusEnum.PAID)
                .count();
    }
}
