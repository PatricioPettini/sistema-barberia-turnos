package com.pato.service;

import com.pato.dto.request.BarberoRequest;
import com.pato.dto.response.BarberoResponse;
import com.pato.helpers.mappers.BarberoMapper;
import com.pato.model.Barbero;
import com.pato.repository.IBarberoRepository;
import com.pato.service.implementations.BarberoService;
import com.pato.validation.BarberoValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BarberoServiceTest {

    @Mock
    private IBarberoRepository barberoRepository;

    @Mock
    private BarberoMapper barberoMapper;

    @Mock
    private BarberoValidation barberoValidation;

    @InjectMocks
    private BarberoService barberoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crear_ok() {
        BarberoRequest req = new BarberoRequest("Juan", "Perez", "1122334455");
        Barbero entity = new Barbero(1L, "Juan", "Perez", "1122334455", true);
        BarberoResponse resp = new BarberoResponse(1L, "Juan", "Perez", "1122334455", true);

        when(barberoMapper.fromRequestToEntity(req)).thenReturn(entity);
        when(barberoRepository.save(entity)).thenReturn(entity);
        when(barberoMapper.toResponseDto(entity)).thenReturn(resp);

        BarberoResponse result = barberoService.crear(req);

        verify(barberoValidation).validarTelefonoCrear("1122334455");
        verify(barberoRepository).save(entity);

        assertEquals("Juan", result.getNombre());
    }

    @Test
    void buscarPorId_ok() {
        Barbero entity = new Barbero(1L, "Luis", "Lopez", "1199887766", true);
        BarberoResponse resp = new BarberoResponse(1L, "Luis", "Lopez", "1199887766", true);

        when(barberoRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(barberoMapper.toResponseDto(entity)).thenReturn(resp);

        BarberoResponse result = barberoService.buscarPorId(1L);

        assertEquals("Luis", result.getNombre());
    }

    @Test
    void buscarPorId_errorNoExiste() {
        when(barberoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> barberoService.buscarPorId(99L)
        );
    }

    @Test
    void listar_ok() {
        Barbero entity = new Barbero(1L, "Pepe", "Gomez", "1111111111", true);
        BarberoResponse resp = new BarberoResponse(1L, "Pepe", "Gomez", "1111111111", true);

        when(barberoRepository.findAll()).thenReturn(List.of(entity));
        when(barberoMapper.toResponseDto(entity)).thenReturn(resp);

        List<BarberoResponse> result = barberoService.listar();

        assertEquals(1, result.size());
        assertEquals("Pepe", result.get(0).getNombre());
    }

    @Test
    void actualizar_ok() {
        BarberoRequest req = new BarberoRequest("Carlos", "Sanchez", "2211445566");
        Barbero entity = new Barbero(1L, "Pepe", "Gomez", "1111111111", true);
        Barbero updated = new Barbero(1L, "Carlos", "Sanchez", "2211445566", true);
        BarberoResponse resp = new BarberoResponse(1L, "Carlos", "Sanchez", "2211445566", true);

        when(barberoRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(barberoRepository.save(entity)).thenReturn(updated);
        when(barberoMapper.toResponseDto(updated)).thenReturn(resp);

        BarberoResponse result = barberoService.actualizar(1L, req);

        verify(barberoValidation).validarTelefonoActualizar(1L, "2211445566");

        assertEquals("Carlos", result.getNombre());
    }

    @Test
    void cambiarEstado_ok() {
        Barbero entity = new Barbero(1L, "Juan", "Perez", "123", true);

        when(barberoRepository.findById(1L)).thenReturn(Optional.of(entity));

        barberoService.cambiarEstado(1L);

        verify(barberoRepository).save(entity);
        assertFalse(entity.isActivo());
    }
}
