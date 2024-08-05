package com.cidenet.raffleplatform.service.implementation;

import com.cidenet.raffleplatform.dto.RaffleActiveResponseDTO;
import com.cidenet.raffleplatform.dto.RaffleResponseDTO;
import com.cidenet.raffleplatform.enums.RaffleStatusEnum;
import com.cidenet.raffleplatform.model.Raffle;
import com.cidenet.raffleplatform.model.User;
import com.cidenet.raffleplatform.repository.RaffleRepository;
import com.cidenet.raffleplatform.repository.TicketRepository;
import com.cidenet.raffleplatform.repository.UserRepository;
import com.cidenet.raffleplatform.service.interfaces.ITicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para la clase RaffleServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class RaffleServiceImplTest {

    @Mock
    private RaffleRepository raffleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ITicketService ticketService; //

    @InjectMocks
    private RaffleServiceImpl raffleService;

    private Raffle raffle;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);

        raffle = Raffle.builder()
                .id(1L)
                .status(RaffleStatusEnum.ACTIVE)
                .build();
    }


    /**
     * Verifica la creación de una rifa.
     */
    @Test
    void testCreateRaffle() {
        String documentNumber = "123456";
        User user = new User();
        user.setDocumentNumber(documentNumber);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(documentNumber);
        when(userRepository.findByDocumentNumber(documentNumber)).thenReturn(Optional.of(user));

        Raffle raffleRequest = Raffle.builder().name("Rifa de prueba")
                .prizeDescription("Premio de prueba").isDonatedPrize(false)
                .prizeValue(1000).raffleSelection("L").numberOfFigures(10)
                .ticketPrice(50).description("Descripción de la prueba").build();

        when(raffleRepository.save(any(Raffle.class))).thenAnswer(invocation -> {
            Raffle raffle = invocation.getArgument(0);
            raffle.setId(1L);
            return raffle;
        });

        Raffle result = raffleService.createRaffle(raffleRequest);

        assertNotNull(result);
        assertEquals(raffleRequest.getName(), result.getName());
        assertEquals(raffleRequest.getPrizeDescription(), result.getPrizeDescription());
        assertEquals(raffleRequest.getIsDonatedPrize(), result.getIsDonatedPrize());
        assertEquals(raffleRequest.getPrizeValue(), result.getPrizeValue());
        assertEquals(raffleRequest.getRaffleSelection(), result.getRaffleSelection());
        assertEquals(raffleRequest.getNumberOfFigures(), result.getNumberOfFigures());
        assertEquals(raffleRequest.getTicketPrice(), result.getTicketPrice());
        assertEquals(raffleRequest.getStatus(), result.getStatus());
        assertEquals(raffleRequest.getDescription(), result.getDescription());
        assertEquals(user, result.getCreatedBy());

        verify(ticketService).createTicket(result.getId(), result.getNumberOfFigures());
    }


    /**
     * Verifica que se devuelvan las rifas esperadas.
     */
    @Test
    void getAllRafflesReturnsExpectedRaffles() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Object[] raffleData1 = new Object[]{1L, "Rifa 1", "Premio 1", 1000, now, "Creador 1", "Descripción 1",
                RaffleStatusEnum.ACTIVE, 10L, 5L, 2L, 100};
        Object[] raffleData2 = new Object[]{2L, "Rifa 2", "Premio 2", 2000, now, "Creador 2", "Descripción 2",
                RaffleStatusEnum.INACTIVE, 20L, 10L, 4L, 200};
        List<Object[]> expectedRafflesData = Arrays.asList(raffleData1, raffleData2);

        when(raffleRepository.findAllRafflesWithTicketCountsAndOrdered(RaffleStatusEnum.ACTIVE,
                RaffleStatusEnum.INACTIVE))
                .thenReturn(expectedRafflesData);

        List<RaffleResponseDTO> result = raffleService.getAllRaffles();

        assertEquals(expectedRafflesData.size(), result.size());

        // Additional assertions for new field ticketPrice
        assertEquals(raffleData1[11], result.get(0).ticketPrice());
        assertEquals(raffleData2[11], result.get(1).ticketPrice());
    }


    /**
     * Verifica que se devuelva una lista vacía cuando no hay rifas.
     */
    @Test
    void getAllRafflesReturnsEmptyListWhenNoRaffles() throws Exception {
        when(raffleRepository.findAllRafflesWithTicketCountsAndOrdered(RaffleStatusEnum.ACTIVE, RaffleStatusEnum.INACTIVE))
                .thenReturn(Collections.emptyList());

        List<RaffleResponseDTO> result = raffleService.getAllRaffles();

        assertTrue(result.isEmpty());
    }


    /**
     * Verifica que se lance una excepción cuando falla la obtención de las rifas.
     */
    @Test
    void getAllRafflesThrowsExceptionWhenDataAccessFails() {
        when(raffleRepository.findAllRafflesWithTicketCountsAndOrdered(RaffleStatusEnum.ACTIVE, RaffleStatusEnum.INACTIVE))
                .thenThrow(new RuntimeException());

        assertThrows(Exception.class, () -> raffleService.getAllRaffles());
    }


    /**
     * Verifica la obtención de un sorteo por su ID.
     */
    @Test
    void testGetRaffleById() {
        Long id = 1L;
        Raffle raffle = new Raffle();
        raffle.setId(id);
        when(raffleRepository.findById(id)).thenReturn(Optional.of(raffle));

        Optional<Raffle> result = raffleService.getRaffleById(id);

        assertTrue(result.isPresent());
        assertEquals(raffle, result.get());
    }


    /**
     * Verifica la actualización de un sorteo.
     */
    @Test
    void testUpdateRaffle() {
        Long id = 1L;
        User user = new User();
        user.setId(1L);

        Raffle existingRaffle = Raffle.builder().id(id).name("Rifa existente").prizeDescription("Premio existente")
                .isDonatedPrize(true).prizeValue(500).creationDate(LocalDateTime.now().minusDays(1))
                .raffleDate(LocalDateTime.now().plusDays(1)).raffleSelection("S").numberOfFigures(10).ticketPrice(20)
                .winnerNumber(5).status(RaffleStatusEnum.ACTIVE).description("Descripción existente")
                .image("imagen_existente.jpg").createdBy(user).build();

        Raffle updatedRaffleRequest = Raffle.builder().name("Rifa actualizado").prizeDescription("Premio actualizado")
                .raffleSelection("L").build();

        when(raffleRepository.findById(id)).thenReturn(Optional.of(existingRaffle));
        when(raffleRepository.save(any(Raffle.class))).thenAnswer(invocation -> {
            Raffle raffle = invocation.getArgument(0);
            raffle.setId(id);
            return raffle;
        });

        Raffle updatedRaffle = raffleService.updateRaffle(id, updatedRaffleRequest);

        assertNotNull(updatedRaffle);
        assertEquals(existingRaffle.getId(), updatedRaffle.getId());
        assertEquals(updatedRaffleRequest.getName(), updatedRaffle.getName());
        assertEquals(updatedRaffleRequest.getPrizeDescription(), updatedRaffle.getPrizeDescription());
        assertEquals(updatedRaffleRequest.getRaffleSelection(), updatedRaffle.getRaffleSelection());
        assertEquals(existingRaffle.getIsDonatedPrize(), updatedRaffle.getIsDonatedPrize());
        assertEquals(existingRaffle.getPrizeValue(), updatedRaffle.getPrizeValue());
        assertEquals(existingRaffle.getCreationDate(), updatedRaffle.getCreationDate());
        assertEquals(existingRaffle.getRaffleDate(), updatedRaffle.getRaffleDate());
        assertEquals(existingRaffle.getNumberOfFigures(), updatedRaffle.getNumberOfFigures());
        assertEquals(existingRaffle.getTicketPrice(), updatedRaffle.getTicketPrice());
        assertEquals(existingRaffle.getWinnerNumber(), updatedRaffle.getWinnerNumber());
        assertEquals(existingRaffle.getStatus(), updatedRaffle.getStatus());
        assertEquals(existingRaffle.getDescription(), updatedRaffle.getDescription());
        assertEquals(existingRaffle.getImage(), updatedRaffle.getImage());
        assertEquals(existingRaffle.getCreatedBy(), updatedRaffle.getCreatedBy());

        when(raffleRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> raffleService.updateRaffle(id, updatedRaffleRequest));
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("Rifa no encontrada con ID: " + id, exception.getCause().getMessage());
    }


    /**
     * Prueba para eliminar lógicamente un sorteo cuando el sorteo está activo.
     */
    @Test
    void testLogicalDeleteRaffle_WhenRaffleIsActive() {
        Long id = 1L;
        Raffle raffle = new Raffle();
        raffle.setId(id);
        raffle.setStatus(RaffleStatusEnum.ACTIVE);

        when(raffleRepository.findById(id)).thenReturn(Optional.of(raffle));
        raffleService.logicalDeleteRaffle(id);

        assertEquals(RaffleStatusEnum.DELETED, raffle.getStatus());
        verify(ticketRepository).deleteByRaffleId(id);
        verify(raffleRepository).save(raffle);
    }


    /**
     * Prueba para eliminar lógicamente un sorteo cuando el sorteo está inactivo.
     */
    @Test
    void testLogicalDeleteRaffle_WhenRaffleIsInactive() {
        Long id = 1L;
        Raffle raffle = new Raffle();
        raffle.setId(id);
        raffle.setStatus(RaffleStatusEnum.INACTIVE);

        when(raffleRepository.findById(id)).thenReturn(Optional.of(raffle));
        raffleService.logicalDeleteRaffle(id);

        assertEquals(RaffleStatusEnum.DELETED, raffle.getStatus());
        verify(ticketRepository).deleteByRaffleId(id);
        verify(raffleRepository).save(raffle);
    }


    /**
     * Prueba para eliminar lógicamente un sorteo cuando el sorteo no está activo ni
     * inactivo.
     */
    @Test
    void testLogicalDeleteRaffle_WhenRaffleIsNotActiveOrInactive() {
        Long id = 1L;
        Raffle raffle = new Raffle();
        raffle.setId(id);
        raffle.setStatus(RaffleStatusEnum.FINISHED);

        when(raffleRepository.findById(id)).thenReturn(Optional.of(raffle));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> raffleService.logicalDeleteRaffle(id));
        assertEquals("La rifa no se puede eliminar porque no está en estado activo o inactivo.",
                exception.getMessage());
        verify(raffleRepository, never()).save(any(Raffle.class));
        verify(ticketRepository, never()).deleteByRaffleId(id);
    }


    /**
     * Verifica la inactivación de una rifa.
     */
    @Test
    public void testInactivateRaffle_Success() {
        Long raffleId = 1L;
        Raffle raffle = new Raffle();
        raffle.setId(raffleId);
        raffle.setStatus(RaffleStatusEnum.ACTIVE);

        when(raffleRepository.findById(raffleId)).thenReturn(Optional.of(raffle));

        raffleService.inactivateRaffle(raffleId);

        verify(raffleRepository, times(1)).save(raffle);
        assertEquals(RaffleStatusEnum.INACTIVE, raffle.getStatus());
    }


    /**
     * Verifica que se lance una excepción cuando se intenta inactivar una rifa que
     * no existe.
     */
    @Test
    public void testInactivateRaffle_NotFound() {
        Long raffleId = 1L;

        when(raffleRepository.findById(raffleId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            raffleService.inactivateRaffle(raffleId);
        });

        assertEquals("Rifa no encontrada con ID: " + raffleId, exception.getMessage());
    }


    /**
     * Verifica que se lance una excepción cuando se intenta inactivar una rifa que
     * no está activa.
     */
    @Test
    void getRafflesByStatusAndFutureRaffleDate_ReturnsExpectedRaffles() {
        // Given
        Raffle raffle1 = new Raffle();
        raffle1.setId(1L);
        raffle1.setTickets(new ArrayList<>()); // Initialize the tickets list
        Raffle raffle2 = new Raffle();
        raffle2.setId(2L);
        raffle2.setTickets(new ArrayList<>()); // Initialize the tickets list
        List<Raffle> raffles = Arrays.asList(raffle1, raffle2);

        when(raffleRepository.findAllByStatusAndFutureRaffleDate(RaffleStatusEnum.ACTIVE)).thenReturn(raffles);

        // When
        List<RaffleActiveResponseDTO> result = raffleService.getRafflesByStatusAndFutureRaffleDate(RaffleStatusEnum.ACTIVE);

        // Then
        assertEquals(raffles.size(), result.size());
    }


    /**
     * Verifica que se devuelva una lista vacía cuando no hay rifas.
     */
    @Test
    void getRafflesByStatusAndFutureRaffleDate_ThrowsRuntimeException_WhenUnexpectedErrorOccurs() {
        when(raffleRepository.findAllByStatusAndFutureRaffleDate(RaffleStatusEnum.ACTIVE)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> raffleService.getRafflesByStatusAndFutureRaffleDate(RaffleStatusEnum.ACTIVE));
    }
}
