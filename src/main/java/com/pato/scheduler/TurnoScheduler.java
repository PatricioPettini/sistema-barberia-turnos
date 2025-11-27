package com.pato.scheduler;

import com.pato.model.Turno;
import com.pato.model.enums.EstadoTurno;
import com.pato.repository.ITurnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TurnoScheduler {

    private final ITurnoRepository turnoRepository;

    @Scheduled(fixedRate = 30 * 60 * 1000) // Cada 30 minutos
    public void finalizarTurnosPasados() {

        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        List<Turno> turnos = turnoRepository.findByFechaAndHoraHastaBeforeAndEstadoTurnoNot(
                hoy,
                ahora,
                EstadoTurno.CANCELADO
        );

        for (Turno turno : turnos) {

            if (turno.getEstadoTurno() != EstadoTurno.FINALIZADO) {
                turno.setEstadoTurno(EstadoTurno.FINALIZADO);
            }
        }

        turnoRepository.saveAll(turnos);

        System.out.println("Scheduler ejecutado: turnos finalizados -> " + turnos.size());
    }
}
