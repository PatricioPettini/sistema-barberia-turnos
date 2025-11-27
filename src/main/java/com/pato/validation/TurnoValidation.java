package com.pato.validation;

import com.pato.model.Turno;
import com.pato.model.enums.EstadoTurno;
import com.pato.repository.ITurnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TurnoValidation {

    private final ITurnoRepository turnoRepository;

    public void validarEstadoTurno(EstadoTurno estado){
        if(estado.equals(EstadoTurno.CANCELADO)){
            throw new IllegalArgumentException("El turno no puede editarse porque fue cancelado");
        }

        if(estado.equals(EstadoTurno.FINALIZADO)){
            throw new IllegalArgumentException("El turno no puede editarse porque ya fue finalizado");
        }

        if(estado.equals(EstadoTurno.NO_ASISTIO)){
            throw new IllegalArgumentException("El turno no puede editarse porque el cliente no asistió. Debe crear un nuevo turno!");
        }
    }

    public void validarHorarioTurno(LocalDate fecha, LocalTime hora) {

        LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

        // 1. No permitir fechas pasadas
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha y hora del turno debe ser futura.");
        }

        // 2. Validar día permitido (martes a sábado)
        DayOfWeek dia = fecha.getDayOfWeek();
        if (dia == DayOfWeek.SUNDAY || dia == DayOfWeek.MONDAY) {
            throw new IllegalArgumentException("Solo se pueden sacar turnos de martes a sábado.");
        }

        // 3. Validar rango horario
        LocalTime apertura = LocalTime.of(10, 0);
        LocalTime cierre = LocalTime.of(20, 0);

        if (hora.isBefore(apertura) || hora.isAfter(cierre)) {
            throw new IllegalArgumentException("El horario debe estar entre las 10:00 y las 20:00.");
        }
    }

    public void validarHorarioYBarberoDisponible(
            LocalDate fecha,
            LocalTime horaDesde,
            int duracion,
            Long idBarbero
    ) {

        LocalTime horaHasta = horaDesde.plusMinutes(duracion);

        List<Turno> solapados = turnoRepository.findSolapados(
                idBarbero,
                fecha,
                horaDesde,
                horaHasta
        );

        if (!solapados.isEmpty()) {
            throw new IllegalArgumentException(
                    "El horario seleccionado no está disponible para este barbero."
            );
        }
    }

    public void validarFechaFutura(LocalDate fecha, LocalTime horaInicio){

        LocalDateTime fechaHora=LocalDateTime.of(fecha,horaInicio);
        LocalDateTime present=LocalDateTime.now();

        if(fechaHora.isBefore(present)){
            throw new IllegalArgumentException("La fecha no puede ser anterior a la actual");
        }
    }
}
