package com.pato.scheduler;

import com.pato.model.Turno;
import com.pato.model.enums.EstadoTurno;
import com.pato.repository.ITurnoRepository;
import com.pato.service.implementations.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TurnoReminderScheduler {

    private final ITurnoRepository turnoRepository;
    private final EmailService emailService;

    @Scheduled(fixedRate = 30 * 60 * 1000)  // cada 30 min
    public void enviarRecordatorios() {

        LocalDate hoy = LocalDate.now();
        LocalTime desde = LocalTime.now().plusHours(2);
        LocalTime hasta = LocalTime.now().plusHours(3);

        List<Turno> turnos = turnoRepository
                .findByFechaAndHoraDesdeBetweenAndEstadoTurnoAndRecordatorioEnviadoFalse(
                        hoy,
                        desde,
                        hasta,
                        EstadoTurno.PENDIENTE
                );

        for (Turno turno : turnos) {

            String fechaStr = turno.getFecha().toString();
            String horaStr  = turno.getHoraDesde().toString().substring(0, 5);

            String servicioNombre = turno.getServicio().getNombreServicio();
            String barberoNombre  = turno.getBarbero().getNombre() + " " +
                    turno.getBarbero().getApellido();

            String linkCancelar = "http://localhost:8082/cancelar-turno.html?id=" + turno.getIdTurno();


            String html = emailService.cargarRecordatorioTurno(
                    turno.getNombreCliente(),
                    fechaStr,
                    horaStr,
                    servicioNombre,
                    barberoNombre,
                    linkCancelar
            );

            emailService.enviarHtml(
                    turno.getEmailCliente(),
                    "Recordatorio de tu turno – Style Barber",
                    html
            );


            emailService.enviarHtml(
                    turno.getEmailCliente(),
                    "Recordatorio de Turno",
                    html
            );

            turno.setRecordatorioEnviado(true);
        }

        turnoRepository.saveAll(turnos);

        System.out.println("Recordatorios enviados: " + turnos.size());
    }
}

