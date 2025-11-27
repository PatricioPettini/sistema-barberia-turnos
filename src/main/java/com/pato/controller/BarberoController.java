package com.pato.controller;

import com.pato.dto.request.BarberoRequest;
import com.pato.dto.response.BarberoResponse;
import com.pato.service.interfaces.IBarberoService;
import com.pato.service.interfaces.IGenericService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barberos")
@RequiredArgsConstructor
@EnableMethodSecurity
public class BarberoController {

    private final IBarberoService barberoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BarberoResponse> crear(@Valid @RequestBody BarberoRequest request) {
        return ResponseEntity.ok(barberoService.crear(request));
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<BarberoResponse>> listar() {
        return ResponseEntity.ok(barberoService.listar());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<BarberoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(barberoService.buscarPorId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BarberoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody BarberoRequest request
    ) {
        return ResponseEntity.ok(barberoService.actualizar(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id) {
        barberoService.cambiarEstado(id);
        return ResponseEntity.noContent().build();
    }
}
