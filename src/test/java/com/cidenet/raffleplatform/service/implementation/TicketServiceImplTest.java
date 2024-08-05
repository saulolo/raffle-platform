package com.cidenet.raffleplatform.service.implementation;

import com.cidenet.raffleplatform.dto.TicketRaffleResponseDTO;
import com.cidenet.raffleplatform.mapper.TicketMapper;
import com.cidenet.raffleplatform.model.Raffle;
import com.cidenet.raffleplatform.model.Ticket;
import com.cidenet.raffleplatform.repository.RaffleRepository;
import com.cidenet.raffleplatform.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private RaffleRepository raffleRepository;

    @Mock
    private TicketMapper ticketMapper;

    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ticketService = new TicketServiceImpl(ticketRepository, raffleRepository);
    }


    /**
     * Verifica que se creen el número correcto de boletos
     */
    @Test
    void shouldCreateCorrectNumberOfTickets() {
        Long raffleId = 1L;
        int numberOfFigures = 2;
        Raffle raffle = new Raffle();
        when(raffleRepository.findById(raffleId)).thenReturn(Optional.of(raffle));

        List<Ticket> savedTickets = new ArrayList<>();
        for (int i = 1; i <= Math.pow(10, numberOfFigures) - 1; i++) {
            savedTickets.add(new Ticket());
        }
        when(ticketRepository.saveAll(anyList())).thenReturn(savedTickets);

        List<Ticket> createdTickets = ticketService.createTicket(raffleId, numberOfFigures);

        int expectedNumberOfTickets = (int) Math.pow(10, numberOfFigures) - 1;
        assertEquals(expectedNumberOfTickets, createdTickets.size());
    }


    /**
     * Verifica que se lance una excepción si no se encuentra la rifa
     */
    @Test
    void shouldThrowExceptionWhenRaffleNotFound() {
        Long raffleId = 1L;
        int numberOfFigures = 2;
        when(raffleRepository.findById(raffleId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ticketService.createTicket(raffleId, numberOfFigures));
    }


    /**
     * Verifica que se obtenga la cantidad correcta de boletos disponibles
     */
    @Test
    void testGetCountAvailableTickets() {
        Long raffleId = 1L;
        Integer expectedCount = 10;
        when(ticketRepository.countAvailableTicketsByRaffleId(raffleId)).thenReturn(expectedCount);

        Integer result = ticketService.getCountAvailableTickets(raffleId);

        assertEquals(expectedCount, result);
    }


    /**
     * Verifica que se actualicen los boletos cuando cambia el número de cifras
     */
    @Test
    void shouldUpdateTicketsWhenNumberOfFiguresChanges() {
        Long raffleId = 1L;
        int numberOfFigures = 3;
        Raffle raffle = new Raffle();
        when(raffleRepository.findById(raffleId)).thenReturn(Optional.of(raffle));
        when(ticketRepository.countAvailableTicketsByRaffleId(raffleId)).thenReturn(100);

        ticketService.updateTickets(raffleId, numberOfFigures);

        verify(ticketRepository, times(1)).deleteByRaffleIdInBatch(anyLong(), anyInt(), anyInt());
        verify(ticketRepository, times(1)).saveAll(anyList());
    }


    /**
     * Verifica que se lance una excepción si no se encuentra la rifa en el método de actualización
     */
    @Test
    void shouldThrowExceptionWhenRaffleNotFoundInUpdateTickets() {
        Long raffleId = 1L;
        int numberOfFigures = 2;
        when(raffleRepository.findById(raffleId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ticketService.updateTickets(raffleId, numberOfFigures));
    }


    /**
     * Verifica que se lance una excepción si ocurre un error al actualizar los boletos
     */
    @Test
    public void testCreateTicketBatch() {
        Long raffleId = 1L;
        int numberOfFigures = 3;
        int start = 0;
        int batchSize = 100;

        Raffle raffle = new Raffle();
        raffle.setId(raffleId);

        when(raffleRepository.findById(raffleId)).thenReturn(Optional.of(raffle));

        ticketService.createTicketBatch(raffleId, numberOfFigures, start, batchSize);

        verify(raffleRepository, times(1)).findById(raffleId);
        verify(ticketRepository, atLeastOnce()).saveAll(anyList());
    }


    /**
     * Verifica que se eliminen los boletos de una rifa en un rango específico
     */
    @Test
    public void testDeleteByRaffleIdInBatch() {
        Long raffleId = 1L;
        int start = 0;
        int batchSize = 100;

        when(ticketRepository.findTicketsByRaffleIdInRange(raffleId, start, batchSize)).thenReturn(new ArrayList<>());

        ticketService.deleteByRaffleIdInBatch(raffleId, start, batchSize);

        verify(ticketRepository, times(1)).findTicketsByRaffleIdInRange(raffleId, start, batchSize);
        verify(ticketRepository, atLeastOnce()).deleteAll(anyList());
    }


    /**
     * Verifica que se obtengan los boletos de una rifa
     */
    @Test
    void getTicketsByRaffleId_ReturnsExpectedTickets() {
        // Given
        Long raffleId = 1L;
        Raffle raffle = new Raffle();
        raffle.setId(raffleId);
        raffle.setName("Test Raffle");

        Ticket ticket1 = new Ticket();
        ticket1.setId(1L);
        ticket1.setRaffle(raffle); // Set the raffle object

        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);
        ticket2.setRaffle(raffle); // Set the raffle object

        List<Ticket> tickets = Arrays.asList(ticket1, ticket2);

        when(ticketRepository.findByRaffleId(raffleId)).thenReturn(tickets);

        // When
        List<TicketRaffleResponseDTO> result = ticketService.getTicketsByRaffleId(raffleId);

        // Then
        assertEquals(tickets.size(), result.size());
    }


    /**
     * Verifica que se lance una excepción si el id de la rifa es inválido
     */
    @Test
    void testGetTicketsByRaffleId_InvalidId() {
        Long invalidRaffleId = -1L;

        assertThrows(RuntimeException.class, () -> ticketService.getTicketsByRaffleId(invalidRaffleId));
    }

    /**
     * Verifica que se lance una excepción si ocurre un error al obtener los boletos
     */
    @Test
    void testGetTicketsByRaffleId_DataAccessException() {
        Long raffleId = 1L;

        when(ticketRepository.findByRaffleId(raffleId)).thenThrow(new DataAccessException("...") {
        });

        assertThrows(RuntimeException.class, () -> ticketService.getTicketsByRaffleId(raffleId));
    }

    /**
     * Verifica que se lance una excepción si ocurre un error inesperado al obtener los boletos
     */
    @Test
    void testGetTicketsByRaffleId_UnexpectedException() {
        Long raffleId = 1L;

        when(ticketRepository.findByRaffleId(raffleId)).thenThrow(new RuntimeException("Unexpected error"));

        assertThrows(RuntimeException.class, () -> ticketService.getTicketsByRaffleId(raffleId));
    }
}
