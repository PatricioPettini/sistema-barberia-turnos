package com.pato.validation;

import com.pato.model.Turno;
import com.pato.model.enums.EstadoTurno;
import com.pato.repository.ITurnoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.*;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TurnoValidationTest {

    private ITurnoRepository turnoRepository;
    private TurnoValidation validation;

    @BeforeEach
    void setup() {
        turnoRepository = mock(ITurnoRepository.class);
        validation = new TurnoValidation(turnoRepository);
    }

    @Test
    void validarEstadoTurno_cancelado() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validation.validarEstadoTurno(EstadoTurno.CANCELADO)
        );
        assertTrue(ex.getMessage().contains("cancelado"));
    }

    @Test
    void validarEstadoTurno_finalizado() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validation.validarEstadoTurno(EstadoTurno.FINALIZADO)
        );
        assertTrue(ex.getMessage().contains("finalizado"));
    }

    @Test
    void validarEstadoTurno_noAsistio() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validation.validarEstadoTurno(EstadoTurno.NO_ASISTIO)
        );
        assertTrue(ex.getMessage().contains("no asistió"));
    }

    @Test
    void validarEstadoTurno_ok() {
        assertDoesNotThrow(
                () -> validation.validarEstadoTurno(EstadoTurno.PENDIENTE)
        );
    }

    @Test
    void validarHorarioTurno_fechaPasada() {
        LocalDate fecha = LocalDate.now().minusDays(1);
        LocalTime hora = LocalTime.of(12, 0);

        assertThrows(IllegalArgumentException.class,
                () -> validation.validarHorarioTurno(fecha, hora));
    }

    @Test
    void validarHorarioTurno_diaInvalido_domingo() {
        LocalDate fecha = LocalDate.of(2030, 1, 6);
        LocalTime hora = LocalTime.of(12, 0);

        assertThrows(IllegalArgumentException.class,
                () -> validation.validarHorarioTurno(fecha, hora));
    }

    @Test
    void validarHorarioTurno_fueraDeHorario() {
        LocalDate fecha = LocalDate.of(2030, 1, 7);
        LocalTime hora = LocalTime.of(9, 0);

        assertThrows(IllegalArgumentException.class,
                () -> validation.validarHorarioTurno(fecha, hora));
    }

    @Test
    void validarHorarioTurno_ok() {
        LocalDate fecha = LocalDate.of(2030, 1, 2);
        LocalTime hora = LocalTime.of(12, 0);

        assertDoesNotThrow(() -> validation.validarHorarioTurno(fecha, hora));
    }

    @Test
    void validarHorarioYBarberoDisponible_solapado() {

        when(turnoRepository.findSolapados(anyLong(), any(), any(), any()))
                .thenReturn(List.of(new Turno()));

        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(14, 0);
        int duracion = 30;
        Long idBarbero = 1L;

        assertThrows(IllegalArgumentException.class,
                () -> validation.validarHorarioYBarberoDisponible(
                        fecha,
                        hora,
                        duracion,
                        idBarbero
                )
        );
    }

    @Test
    void validarHorarioYBarberoDisponible_ok() {
        when(turnoRepository.findSolapados(
                anyLong(), any(), any(), any()
        )).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() ->
                validation.validarHorarioYBarberoDisponible(
                        LocalDate.now().plusDays(1),
                        LocalTime.of(14, 0),
                        30,
                        1L
                ));
    }

    @Test
    void validarFechaFutura_pasada() {
        LocalDate fecha = LocalDate.now().minusDays(1);
        LocalTime hora = LocalTime.of(12, 0);

        assertThrows(IllegalArgumentException.class,
                () -> validation.validarFechaFutura(fecha, hora));
    }

    @Test
    void validarFechaFutura_ok() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(12, 0);

        assertDoesNotThrow(() ->
                validation.validarFechaFutura(fecha, hora));
    }
}