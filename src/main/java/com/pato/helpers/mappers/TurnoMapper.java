package com.pato.helpers.mappers;

import com.pato.dto.request.TurnoRequest;
import com.pato.dto.response.TurnoResponse;
import com.pato.model.Barbero;
import com.pato.model.Servicio;
import com.pato.model.Turno;
import com.pato.model.enums.EstadoTurno;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TurnoMapper {

    private final ServicioMapper servicioMapper;

    public Turno toEntity(TurnoRequest dto, Barbero barbero, Servicio servicio) {
        return Turno.builder()
                .nombreCliente(dto.getNombreCliente())
                .telefonoCliente(dto.getTelefonoCliente())
                .emailCliente(dto.getEmailCliente())
                .fecha(dto.getFecha())
                .horaDesde(dto.getHora())
                .horaHasta(dto.getHora().plusMinutes(servicio.getDuracionMinutos()))
                .barbero(barbero)
                .servicio(servicio)
                .estadoTurno(EstadoTurno.PENDIENTE)
                .build();
    }

    public TurnoResponse toResponse(Turno turno) {
        return TurnoResponse.builder()
                .idTurno(turno.getIdTurno())
                .nombreCliente(turno.getNombreCliente())
                .emailCliente(turno.getEmailCliente())
                .telefonoCliente(turno.getTelefonoCliente())
                .fecha(turno.getFecha())
                .horaDesde(turno.getHoraDesde())
                .horaHasta(turno.getHoraHasta())
                .barbero(turno.getBarbero().getNombre())
                .servicio(servicioMapper.toResponseDto(turno.getServicio()))
                .estado(turno.getEstadoTurno())
                .build();
    }
}

