package com.pato.service.implementations;

import com.pato.dto.request.ServicioRequest;
import com.pato.dto.response.ServicioResponse;
import com.pato.helpers.mappers.ServicioMapper;
import com.pato.model.Servicio;
import com.pato.repository.IServicioRepository;
import com.pato.service.interfaces.IServicioService;
import com.pato.validation.ServicioValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioService implements IServicioService {

    private final IServicioRepository servicioRepository;
    private final ServicioMapper servicioMapper;
    private final ServicioValidation servicioValidation;

    @Override
    public void cambiarEstado(Long id) {
        Servicio servicio = getServicioEntity(id);
        servicio.setActivo(!servicio.isActivo());
        servicioRepository.save(servicio);
    }

    @Override
    public ServicioResponse crear(ServicioRequest objeto) {

        servicioValidation.validarNombreUnicoCrear(objeto.getNombreServicio());

        Servicio servicio = servicioMapper.fromRequestToEntity(objeto);
        Servicio guardado= servicioRepository.save(servicio);

        return servicioMapper.toResponseDto(guardado);
    }

    @Override
    public ServicioResponse buscarPorId(Long id) {
        Servicio servicio = getServicioEntity(id);

        return servicioMapper.toResponseDto(servicio);
    }

    public Servicio getServicioEntity(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("No existe barbero con ese id"));
    }

    @Override
    public List<ServicioResponse> listar() {
        return servicioRepository.findAll()
                .stream()
                .map(servicioMapper::toResponseDto)
                .toList();
    }

    @Override
    public ServicioResponse actualizar(Long id, ServicioRequest request) {

        Servicio servicio = getServicioEntity(id);

        servicioValidation.validarNombreUnicoActualizar(id, request.getNombreServicio());

        servicio.setNombreServicio(request.getNombreServicio());
        servicio.setPrecio(request.getPrecio());
        servicio.setDuracionMinutos(request.getDuracionMinutos());

        Servicio guardado = servicioRepository.save(servicio);

        return servicioMapper.toResponseDto(guardado);
    }
}
