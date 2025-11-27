package com.pato.service;

import com.pato.dto.request.TurnoRequest;
import com.pato.dto.response.ServicioResponse;
import com.pato.dto.response.TurnoResponse;
import com.pato.helpers.mappers.TurnoMapper;
import com.pato.model.*;
import com.pato.model.enums.EstadoTurno;
import com.pato.repository.ITurnoRepository;
import com.pato.service.implementations.*;
import com.pato.validation.TurnoValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TurnoServiceTest {

    @Mock private BarberoService barberoService;
    @Mock private ServicioService servicioService;
    @Mock private ITurnoRepository turnoRepository;
    @Mock private TurnoMapper turnoMapper;
    @Mock private TurnoValidation turnoValidation;
    @Mock private EmailService emailService;

    @InjectMocks
    private TurnoService turnoService;

    private Barbero barbero;
    private Servicio servicio;
    private Turno turno;
    private TurnoRequest request;
    private TurnoResponse response;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        barbero = new Barbero(1L, "Juan", "Perez", "1122334455", true);
        servicio = new Servicio(1L, "Corte", 2000, 45, true);

        turno = Turno.builder()
                .idTurno(1L)
                .nombreCliente("Pato")
                .telefonoCliente("1122334455")
                .emailCliente("pato@mail.com")
                .fecha(LocalDate.now())
                .horaDesde(LocalTime.of(14,0))
                .horaHasta(LocalTime.of(14,45))
                .barbero(barbero)
                .servicio(servicio)
                .estadoTurno(EstadoTurno.PENDIENTE)
                .build();

        request = new TurnoRequest(
                "Pato",
                "1122334455",
                "pato@mail.com",
                LocalDate.now(),
                LocalTime.of(14, 0),
                1L,
                1L
        );

        ServicioResponse servicioResponse= new ServicioResponse();
        servicioResponse.setNombreServicio("Corte");
        response = new TurnoResponse(
                1L,
                "Pato",
                "1122334455",
                "pato@mail.com",
                LocalDate.now(),
                LocalTime.of(14,0),
                LocalTime.of(14,45),
                "Juan Perez",
                servicioResponse,
                EstadoTurno.PENDIENTE
        );
    }

    @Test
    void crear_ok() {

        when(barberoService.getBarberoEntity(1L)).thenReturn(barbero);
        when(servicioService.getServicioEntity(1L)).thenReturn(servicio);

        doNothing().when(turnoValidation)
                .validarHorarioTurno(any(), any());

        doNothing().when(turnoValidation)
                .validarFechaFutura(any(), any());

        doNothing().when(turnoValidation)
                .validarHorarioYBarberoDisponible(any(), any(), anyInt(), anyLong());

        when(turnoMapper.toEntity(request, barbero, servicio))
                .thenReturn(turno);

        when(turnoRepository.save(turno)).thenReturn(turno);

        when(emailService.cargarConfirmacion(
                any(), any(), any(), any(), any(), anyLong()
        )).thenReturn("<html>ok</html>");

        doNothing().when(emailService).enviarHtml(any(), any(), any());

        when(turnoMapper.toResponse(turno)).thenReturn(response);

        TurnoResponse result = turnoService.crear(request);

        assertEquals("Pato", result.getNombreCliente());
        verify(turnoRepository).save(turno);
        verify(emailService).enviarHtml(any(), any(), any());
    }

    @Test
    void buscarPorId_ok() {
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));
        when(turnoMapper.toResponse(turno)).thenReturn(response);

        TurnoResponse result = turnoService.buscarPorId(1L);

        assertEquals(1L, result.getIdTurno());
        verify(turnoRepository).findById(1L);
    }

    @Test
    void buscarPorId_noExiste() {
        when(turnoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> turnoService.buscarPorId(99L));
    }

    @Test
    void listar_ok() {

        when(turnoRepository.findAll()).thenReturn(List.of(turno));
        when(turnoMapper.toResponse(turno)).thenReturn(response);

        List<TurnoResponse> lista = turnoService.listar();

        assertEquals(1, lista.size());
        assertEquals("Pato", lista.get(0).getNombreCliente());
    }

    @Test
    void actualizar_ok() {

        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));
        when(barberoService.getBarberoEntity(1L)).thenReturn(barbero);
        when(servicioService.getServicioEntity(1L)).thenReturn(servicio);

        doNothing().when(turnoValidation).validarEstadoTurno(any());
        doNothing().when(turnoValidation)
                .validarHorarioTurno(any(), any());
        doNothing().when(turnoValidation)
                .validarFechaFutura(any(), any());
        doNothing().when(turnoValidation)
                .validarHorarioYBarberoDisponible(any(), any(), anyInt(), anyLong());

        when(turnoRepository.save(any(Turno.class))).thenReturn(turno);
        when(turnoMapper.toResponse(turno)).thenReturn(response);

        TurnoResponse result = turnoService.actualizar(1L, request);

        assertEquals("Pato", result.getNombreCliente());
        verify(turnoRepository).save(any(Turno.class));
    }

    @Test
    void cancelar_ok() {
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));
        when(turnoRepository.save(turno)).thenReturn(turno);
        when(turnoMapper.toResponse(turno)).thenReturn(response);

        TurnoResponse result = turnoService.cancelar(1L);

        assertEquals(EstadoTurno.CANCELADO, turno.getEstadoTurno());
        verify(turnoRepository).save(turno);
    }

    @Test
    void listarPorBarberoYFecha_ok() {

        when(turnoRepository.findByBarberoIdBarberoAndFecha(1L, request.getFecha()))
                .thenReturn(List.of(turno));

        when(turnoMapper.toResponse(turno)).thenReturn(response);

        List<TurnoResponse> lista =
                turnoService.listarPorBarberoYFecha(1L, request.getFecha());

        assertEquals(1, lista.size());
    }

    @Test
    void listarPorFecha_ok() {

        when(turnoRepository.findByFecha(request.getFecha()))
                .thenReturn(List.of(turno));

        when(turnoMapper.toResponse(turno)).thenReturn(response);

        List<TurnoResponse> lista =
                turnoService.listarPorFecha(request.getFecha());

        assertEquals(1, lista.size());
    }
}
