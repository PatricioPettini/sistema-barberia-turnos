package com.pato.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicioResponse {
    private Long idServicio;
    private String nombreServicio;
    private double precio;
    private int duracionMinutos;
    private boolean activo;
}
