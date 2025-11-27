package com.pato.dto.response;

import com.pato.model.enums.EstadoTurno;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurnoResponse {
    private Long idTurno;
    private String nombreCliente;
    private String telefonoCliente;
    private String emailCliente;
    private LocalDate fecha;
    private LocalTime horaDesde;
    private LocalTime horaHasta;
    private String barbero;
    private ServicioResponse servicio;
    private EstadoTurno estado;
}