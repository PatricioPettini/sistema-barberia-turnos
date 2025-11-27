package com.pato.dto.request;

import com.pato.validation.HoraValida;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurnoRequest {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String nombreCliente;

    @NotBlank(message = "El teléfono del cliente es obligatorio")
    @Pattern(regexp = "^[0-9]{8,15}$", message = "Número de teléfono inválido")
    private String telefonoCliente;

    @NotBlank(message = "El email del cliente es obligatorio")
    @Email(message = "Formato de email inválido")
    private String emailCliente;

    @NotNull(message = "La fecha del turno es obligatoria")
    @FutureOrPresent(message = "No puede ser una fecha anterior")
    private LocalDate fecha;

    @NotNull(message = "La hora de inicio es obligatoria")
    @HoraValida
    private LocalTime hora;


    @NotNull(message = "Debe seleccionar un barbero")
    private Long idBarbero;

    @NotNull(message = "Debe seleccionar un servicio")
    private Long idServicio;
}