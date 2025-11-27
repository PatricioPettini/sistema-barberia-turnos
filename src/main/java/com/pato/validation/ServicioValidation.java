package com.pato.validation;

import com.pato.repository.IServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServicioValidation {

    private final IServicioRepository servicioRepository;

    public void validarNombreUnicoCrear(String nombre) {
        if (servicioRepository.existsByNombreServicio(nombre)) {
            throw new IllegalArgumentException("Ya existe un servicio con ese nombre.");
        }
    }

    public void validarNombreUnicoActualizar(Long id, String nombre) {
        if (servicioRepository.existsByNombreServicioAndIdServicioNot(nombre, id)) {
            throw new IllegalArgumentException("Ya existe otro servicio con ese nombre.");
        }
    }
}
