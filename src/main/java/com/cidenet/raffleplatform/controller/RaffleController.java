package com.cidenet.raffleplatform.controller;

import com.cidenet.raffleplatform.dto.RaffleActiveResponseDTO;
import com.cidenet.raffleplatform.dto.RaffleDetailResponseDTO;
import com.cidenet.raffleplatform.dto.RaffleResponseDTO;
import com.cidenet.raffleplatform.dto.RaffleStatusRequestDTO;
import com.cidenet.raffleplatform.mapper.RaffleMapper;
import com.cidenet.raffleplatform.model.Raffle;
import com.cidenet.raffleplatform.service.interfaces.IRaffleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Contiene las operaciones relacionadas con las rifas
 */
@RestController
@RequestMapping("/v1/raffle")
@Tag(name = "Raffle", description = "Operaciones relacionadas con sorteos")
public class RaffleController {

    private final IRaffleService iRaffleService;

    public RaffleController(IRaffleService iRaffleService) {
        this.iRaffleService = iRaffleService;
    }

    /**
     * Crea una rifa
     * @param raffle Rifa a crear
     * @return Rifa creada
     * @throws Exception Excepción en caso de error al crear la rifa en la base de
     *                   datos
     */
    @PostMapping("/create")
    @Operation(summary = "Crear una rifa", description = "Crear una rifa en el sistema")
    public ResponseEntity<RaffleDetailResponseDTO> createRaffle(@Valid @RequestBody Raffle raffle) {
        try {
            Raffle createdRaffle = iRaffleService.createRaffle(raffle);
            RaffleDetailResponseDTO raffleResponseDTO = RaffleMapper.toRaffleDetailResponseDTO(createdRaffle);
            return new ResponseEntity<>(raffleResponseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Obtiene todas las rifas
     * @return Lista de rifas
     * @throws Exception Excepción en caso de error al obtener las rifas de la base
     *                   de datos
     */
    @GetMapping("/all")
    @Operation(summary = "Obtiene todas las rifas", description = "Recuperar todas las rifas en el sistema")
    public ResponseEntity<List<RaffleResponseDTO>> getAllRaffles() {
        try {
            List<RaffleResponseDTO> raffleResponseDTOS = iRaffleService.getAllRaffles();
            return new ResponseEntity<>(raffleResponseDTOS, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Obtiene una rifa por su identificador
     * @param id Identificador de la rifa
     * @return Rifa
     * @throws Exception Excepción en caso de error al obtener la rifa de la base de
     *                   datos
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una rifa por id", description = "Recuperar una rifa por su id en el sistema")
    public ResponseEntity<RaffleDetailResponseDTO> getRaffleById(@PathVariable Long id) {
        try {
            Optional<Raffle> raffle = iRaffleService.getRaffleById(id);
            return raffle.map(r -> ResponseEntity.ok(RaffleMapper.toRaffleDetailResponseDTO(r)))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (RuntimeException e) {
            throw e;
        }
    }


    /**
     * Actualiza una rifa
     * @param id     Identificador de la rifa
     * @param raffle Rifa a actualizar
     * @return Rifa actualizada
     * @throws Exception Excepción en caso de error al actualizar la rifa en la base
     *                   de datos
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un rifa", description =
            "Actualizar una rifa por su id en el sistema")
    public ResponseEntity<RaffleDetailResponseDTO> updateRaffle(@PathVariable Long id,
                                                                @Valid @RequestBody Raffle raffle) {
        try {
            Raffle updatedRaffle = iRaffleService.updateRaffle(id, raffle);
            RaffleDetailResponseDTO raffleResponseDTO = RaffleMapper.toRaffleDetailResponseDTO(updatedRaffle);
            return new ResponseEntity<>(raffleResponseDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Elimina una rifa
     * @param id Identificador de la rifa
     * @return Mensaje de confirmación
     * @throws Exception En caso de error al eliminar la rifa de la base de datos
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Borrado lógico de una rifa",
            description = "Eliminación lógica de una rifa en el sistema por su id")
    public ResponseEntity<?> logicalDeleteRaffle(@PathVariable Long id) {
        try {
            iRaffleService.logicalDeleteRaffle(id);
            return ResponseEntity.ok("La rifa con ID " + id + " fue eliminada exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rifa no encontrada con ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error inesperado: " + e.getMessage());
        }
    }


    /**
     * Inactiva una rifa
     * @param id Identificador de la rifa
     * @return Mensaje de confirmación
     * @throws Exception En caso de error al inactivar la rifa en la base de datos
     */
    @PutMapping("/inactivate/{id}")
    @Operation(summary = "Inactivar una rifa", description = "Inactivar una rifa en el sistema por su id")
    public ResponseEntity<String> inactivateRaffle(@PathVariable Long id) {
        try {
            iRaffleService.inactivateRaffle(id);
            return ResponseEntity.ok("La rifa con ID " + id + " fue inactivada exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rifa no encontrada con ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error inesperado: " + e.getMessage());
        }
    }


    /**
     * Obtiene rifas por estado y fecha de rifa
     * @param requestDTO Datos de la petición
     * @return Lista de rifas
     * @throws Exception Excepción en caso de error al obtener las rifas de la base de datos
     */
    @PostMapping("/status")
    @Operation(summary = "Obtiene rifas por estado y fecha de rifa",
            description = "Recuperar rifas por estado y fecha de rifa futura en el sistema")
    public ResponseEntity<List<RaffleActiveResponseDTO>> getRafflesByStatusAndFutureRaffleDate(
            @Valid @RequestBody RaffleStatusRequestDTO requestDTO) {
        try {
            List<RaffleActiveResponseDTO> raffleActiveResponseDTOS =
                    iRaffleService.getRafflesByStatusAndFutureRaffleDate(requestDTO.raffleStatus());
            return new ResponseEntity<>(raffleActiveResponseDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
