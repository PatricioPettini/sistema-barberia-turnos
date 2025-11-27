package com.pato.service;

import com.pato.dto.request.ServicioRequest;
import com.pato.dto.response.ServicioResponse;
import com.pato.helpers.mappers.ServicioMapper;
import com.pato.model.Servicio;
import com.pato.repository.IServicioRepository;
import com.pato.service.implementations.ServicioService;
import com.pato.validation.ServicioValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicioServiceTest {

    @Mock
    private IServicioRepository servicioRepository;

    @Mock
    private ServicioMapper servicioMapper;

    @Mock
    private ServicioValidation servicioValidation;

    @InjectMocks
    private ServicioService servicioService;

    private Servicio servicio;
    private ServicioRequest request;
    private ServicioResponse response;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        servicio = new Servicio(1L, "Corte básico", 1500, 30, true);
        request = new ServicioRequest("Corte básico", 1500, 30);
        response = new ServicioResponse(1L, "Corte básico", 1500, 30, true);
    }

    @Test
    void crear_ok() {

        doNothing().when(servicioValidation)
                .validarNombreUnicoCrear("Corte básico");

        when(servicioMapper.fromRequestToEntity(request))
                .thenReturn(servicio);

        when(servicioRepository.save(servicio))
                .thenReturn(servicio);

        when(servicioMapper.toResponseDto(servicio))
                .thenReturn(response);

        ServicioResponse result = servicioService.crear(request);

        assertEquals("Corte básico", result.getNombreServicio());
        verify(servicioValidation).validarNombreUnicoCrear("Corte básico");
        verify(servicioRepository).save(servicio);
    }

    @Test
    void buscarPorId_ok() {
        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));
        when(servicioMapper.toResponseDto(servicio)).thenReturn(response);

        ServicioResponse result = servicioService.buscarPorId(1L);

        assertEquals(1L, result.getIdServicio());
        verify(servicioRepository).findById(1L);
    }

    @Test
    void buscarPorId_noExiste() {
        when(servicioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> servicioService.buscarPorId(99L));
    }

    @Test
    void listar_ok() {
        Servicio servicio2 = new Servicio(2L, "Barba", 1200, 20, true);
        ServicioResponse response2 = new ServicioResponse(2L, "Barba", 1200, 20, true);

        when(servicioRepository.findAll()).thenReturn(List.of(servicio, servicio2));
        when(servicioMapper.toResponseDto(servicio)).thenReturn(response);
        when(servicioMapper.toResponseDto(servicio2)).thenReturn(response2);

        List<ServicioResponse> lista = servicioService.listar();

        assertEquals(2, lista.size());
        verify(servicioRepository).findAll();
    }

    @Test
    void actualizar_ok() {

        ServicioRequest reqUpdate = new ServicioRequest("Corte premium", 3000, 45);
        Servicio servicioActualizado = new Servicio(1L, "Corte premium", 3000, 45, true);
        ServicioResponse responseActualizada =
                new ServicioResponse(1L, "Corte premium", 3000, 45, true);

        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));

        doNothing().when(servicioValidation)
                .validarNombreUnicoActualizar(1L, "Corte premium");

        when(servicioRepository.save(any(Servicio.class)))
                .thenReturn(servicioActualizado);

        when(servicioMapper.toResponseDto(servicioActualizado))
                .thenReturn(responseActualizada);

        ServicioResponse result = servicioService.actualizar(1L, reqUpdate);

        assertEquals("Corte premium", result.getNombreServicio());
        assertEquals(3000, result.getPrecio());

        verify(servicioRepository).save(any(Servicio.class));
    }

    @Test
    void cambiarEstado_ok() {

        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));

        servicioService.cambiarEstado(1L);

        assertFalse(servicio.isActivo());
        verify(servicioRepository).save(servicio);
    }
}
