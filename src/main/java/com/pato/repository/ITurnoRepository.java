package com.pato.repository;

import com.pato.model.Turno;
import com.pato.model.enums.EstadoTurno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ITurnoRepository  extends JpaRepository<Turno, Long>{

    List<Turno> findByFechaAndHoraHastaBeforeAndEstadoTurnoNot(
            LocalDate fecha,
            LocalTime hora,
            EstadoTurno estado
    );

    List<Turno> findByFechaAndHoraDesdeBetweenAndEstadoTurno(
            LocalDate fecha,
            LocalTime desde,
            LocalTime hasta,
            EstadoTurno estado
    );

    List<Turno> findByBarberoIdBarberoAndFecha(Long idBarbero, LocalDate fecha);

    @Query("""
    SELECT t FROM Turno t
    WHERE t.barbero.idBarbero = :idBarbero
      AND t.fecha = :fecha
      AND t.estadoTurno IN ('PENDIENTE')
      AND (
            (:horaDesde < t.horaHasta) AND 
            (:horaHasta > t.horaDesde)
          )
""")
    List<Turno> findSolapados(
            @Param("idBarbero") Long idBarbero,
            @Param("fecha") LocalDate fecha,
            @Param("horaDesde") LocalTime horaDesde,
            @Param("horaHasta") LocalTime horaHasta
    );

    List<Turno> findByFechaAndHoraDesdeBetweenAndEstadoTurnoAndRecordatorioEnviadoFalse(
            LocalDate fecha,
            LocalTime desde,
            LocalTime hasta,
            EstadoTurno estadoTurno
    );

    List<Turno> findByFecha(LocalDate fecha);
}