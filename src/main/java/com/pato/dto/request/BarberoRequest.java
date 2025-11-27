package com.pato.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BarberoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 40)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 40)
    private String apellido;

    @Pattern(regexp = "^[0-9]{8,15}$", message = "Telefono inválido")
    private String telefono;
}

