package com.pato.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Barbero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBarbero;
    private String nombre;
    private String apellido;
    @Column(unique = true, nullable = false)
    private String telefono;
    @Column(nullable = false)
    private boolean activo = true;
}