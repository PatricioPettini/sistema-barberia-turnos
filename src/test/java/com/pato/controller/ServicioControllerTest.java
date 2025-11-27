package com.pato.controller;

import com.pato.dto.request.ServicioRequest;
import com.pato.dto.response.ServicioResponse;
import com.pato.service.interfaces.IGenericService;
import com.pato.service.interfaces.IServicioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicioControllerTest {

    @Mock
    private IServicioService servicioService;

    @InjectMocks
    private ServicioController servicioController;

    private ServicioRequest request;
    private ServicioResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request = new ServicioRequest(
                "Corte clásico",
                1500.0,
                30
        );

        response = new ServicioResponse(
                1L,
                "Corte clásico",
                1500.0,
                30,
                true
        );
    }

    @Test
    void crear_ok() {
        when(servicioService.crear(request)).thenReturn(response);

        ResponseEntity<ServicioResponse> result = servicioController.crear(request);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void listar_ok() {
        when(servicioService.listar()).thenReturn(List.of(response));

        ResponseEntity<List<ServicioResponse>> result = servicioController.listar();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());
        assertEquals("Corte clásico", result.getBody().get(0).getNombreServicio());
    }

    @Test
    void buscarPorId_ok() {
        when(servicioService.buscarPorId(1L)).thenReturn(response);

        ResponseEntity<ServicioResponse> result = servicioController.buscarPorId(1L);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1L, result.getBody().getIdServicio());
    }

    @Test
    void actualizar_ok() {
        when(servicioService.actualizar(1L, request)).thenReturn(response);

        ResponseEntity<ServicioResponse> result =
                servicioController.actualizar(1L, request);

        assertEquals(200, result.getStatusCode().value());
        assertEquals("Corte clásico", result.getBody().getNombreServicio());
    }
}
