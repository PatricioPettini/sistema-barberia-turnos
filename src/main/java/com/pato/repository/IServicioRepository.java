package com.pato.repository;

import com.pato.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IServicioRepository extends JpaRepository<Servicio, Long> {
    boolean existsByNombreServicio(String nombreServicio);
    boolean existsByNombreServicioAndIdServicioNot(String nombre, Long id);
}
