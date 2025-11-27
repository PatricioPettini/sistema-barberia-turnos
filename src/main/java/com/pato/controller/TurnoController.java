package com.pato.controller;

import com.pato.dto.request.TurnoRequest;
import com.pato.dto.response.TurnoResponse;
import com.pato.service.interfaces.ITurnoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class TurnoController {

    private final ITurnoService turnoService;

    @PreAuthorize("permitAll()")
    @PostMapping
    public ResponseEntity<TurnoResponse> crear(@Valid @RequestBody TurnoRequest request) {
        return ResponseEntity.ok(turnoService.crear(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{idTurno}")
    public ResponseEntity<TurnoResponse> editar(@PathVariable Long idTurno, @Valid @RequestBody TurnoRequest request) {
        return ResponseEntity.ok(turnoService.actualizar(idTurno, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TurnoResponse>> listar() {
        return ResponseEntity.ok(turnoService.listar());
    }

    @GetMapping("/barbero/{idBarbero}/{fecha}")
    public ResponseEntity<List<TurnoResponse>> listarPorBarberoYFecha(
            @PathVariable Long idBarbero,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(turnoService.listarPorBarberoYFecha(idBarbero, fecha));
    }

    @GetMapping("/barbero/{fecha}")
    public ResponseEntity<List<TurnoResponse>> listarPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(turnoService.listarPorFecha(fecha));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<TurnoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.buscarPorId(id));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<TurnoResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.cancelar(id));
    }
}
