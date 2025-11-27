package com.pato.repository;

import com.pato.model.Barbero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBarberoRepository extends JpaRepository<Barbero, Long> {
    boolean existsByTelefonoAndIdBarberoNot(String telefono, Long id);
    boolean existsByTelefono(String telefono);
}
