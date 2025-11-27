package com.pato.model;

import com.pato.model.enums.EstadoTurno;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTurno;
    private String nombreCliente;
    private String telefonoCliente;
    private String emailCliente;

    private LocalDate fecha;
    private LocalTime horaDesde;
    private LocalTime horaHasta;

    @ManyToOne
    @JoinColumn(name = "id_barbero", nullable = false)
    private Barbero barbero;

    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = false)
    private Servicio servicio;

    @Enumerated(EnumType.STRING)
    private EstadoTurno estadoTurno;

    @Column(nullable = false)
    private boolean activo = true;

    private boolean recordatorioEnviado = false;
}
