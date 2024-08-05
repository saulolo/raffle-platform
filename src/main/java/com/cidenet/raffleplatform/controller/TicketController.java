package com.cidenet.raffleplatform.controller;

import com.cidenet.raffleplatform.dto.TicketCreateRequestDTO;
import com.cidenet.raffleplatform.dto.TicketRaffleResponseDTO;
import com.cidenet.raffleplatform.dto.TicketResponseDTO;
import com.cidenet.raffleplatform.mapper.TicketMapper;
import com.cidenet.raffleplatform.model.Ticket;
import com.cidenet.raffleplatform.service.interfaces.ITicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Contiene las operaciones relacionadas con los tickets
 */
@RestController
@RequestMapping("/v1/ticket")
@Tag(name = "Ticket", description = "Operaciones relacionadas con boletas")
public class TicketController {

    private final ITicketService iTicketService;

    public TicketController(ITicketService iTicketService) {
        this.iTicketService = iTicketService;
    }


    /**
     * Crea un ticket
     * @param request Ticket a crear
     * @return Ticket creado
     * @throws Exception En caso de error al crear el ticket en la base de datos
     */
    @PostMapping("/create")
    @Operation(summary = "Crear una boleta", description = "Crear una boleta en el sistema")
    public ResponseEntity<List<TicketResponseDTO>> createTicket(
            @Valid @RequestBody TicketCreateRequestDTO request) {
        try {
            List<Ticket> ticket = iTicketService.createTicket(request.getRaffleId(),
                    request.getNumberOfFigures());

            List<TicketResponseDTO> ticketResponseDTOS = new ArrayList<>();
            for (Ticket t : ticket) {
                ticketResponseDTOS.add(TicketMapper.toTicketResponseDTO(t));
            }
            return new ResponseEntity<>(ticketResponseDTOS, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * Actualiza los tickets
     * @param request Ticket a actualizar
     * @return Mensaje de éxito o error
     * @throws Exception En caso de error al actualizar los tickets en la base de
     *                   datos
     */
    @PutMapping("/update")
    @Operation(summary = "Actualizar boletas", description = "Actualizar las boletas de una rifa")
    public ResponseEntity<String> updateTickets(@Valid @RequestBody TicketCreateRequestDTO request) {
        try {
            iTicketService.updateTickets(request.getRaffleId(), request.getNumberOfFigures());
            return ResponseEntity.ok("Boletas actualizadas exitosamente");
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * Obtiene los tickets de una rifa
     * @param raffleId Identificador de la rifa
     * @return Lista de tickets
     * @throws RuntimeException en caso de error al obtener los tickets de la base de datos
     * @throws Exception        En caso de error al obtener los tickets de la base de datos
     */
    @GetMapping("/tickets/{raffleId}")
    @Operation(summary = "Obtiene las boletas por id de rifa", description = "Recuperar las boletas por id de rifa.")
    public ResponseEntity<List<TicketRaffleResponseDTO>> getTicketsByRaffleId(@PathVariable Long raffleId) {
        try {
            List<TicketRaffleResponseDTO> tickets = iTicketService.getTicketsByRaffleId(raffleId);
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
