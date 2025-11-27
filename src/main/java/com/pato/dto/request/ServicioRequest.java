package com.pato.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicioRequest {

    @NotNull(message = "El nombre del servicio es obligatorio")
    private String nombreServicio;

    @Positive(message = "El precio debe ser mayor a 0")
    private double precio;

    @Positive(message = "La duración debe ser mayor a 0")
    private int duracionMinutos;
}

