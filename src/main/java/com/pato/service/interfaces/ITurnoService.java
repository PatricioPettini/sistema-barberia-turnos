package com.pato.service.interfaces;

import com.pato.dto.request.TurnoRequest;
import com.pato.dto.response.TurnoResponse;

import java.time.LocalDate;
import java.util.List;

public interface ITurnoService extends IGenericService<TurnoRequest, TurnoResponse>{
    TurnoResponse cancelar(Long id);
    public List<TurnoResponse> listarPorBarberoYFecha(Long idBarbero, LocalDate fecha);
    public List<TurnoResponse> listarPorFecha(LocalDate fecha);

}
