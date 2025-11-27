package com.pato.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BarberoResponse {
    private Long idBarbero;
    private String nombre;
    private String apellido;
    private String telefono;
    private boolean activo;
}
