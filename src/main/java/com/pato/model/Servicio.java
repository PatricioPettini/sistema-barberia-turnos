package com.pato.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicio;
    @Column(unique = true, nullable = false)
    private String nombreServicio;
    @Column(nullable = false)
    private double precio;
    @Column(nullable = false)
    private int duracionMinutos;
    @Column(nullable = false)
    private boolean activo = true;
}
