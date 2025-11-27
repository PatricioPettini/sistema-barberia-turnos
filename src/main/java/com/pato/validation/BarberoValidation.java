package com.pato.validation;

import com.pato.repository.IBarberoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BarberoValidation {

    private final IBarberoRepository barberoRepository;

    public void validarTelefonoCrear(String telefono) {
        if (barberoRepository.existsByTelefono(telefono)) {
            throw new IllegalArgumentException("Ya existe un barbero con ese teléfono.");
        }
    }

    public void validarTelefonoActualizar(Long id, String telefono) {
        if (barberoRepository.existsByTelefonoAndIdBarberoNot(telefono, id)) {
            throw new IllegalArgumentException("Ya existe otro barbero con ese teléfono.");
        }
    }
}
