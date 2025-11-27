package com.pato.service.implementations;

import com.pato.dto.request.BarberoRequest;
import com.pato.dto.response.BarberoResponse;
import com.pato.helpers.mappers.BarberoMapper;
import com.pato.model.Barbero;
import com.pato.repository.IBarberoRepository;
import com.pato.service.interfaces.IBarberoService;
import com.pato.validation.BarberoValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarberoService implements IBarberoService {

    private final IBarberoRepository barberoRepository;
    private final BarberoMapper barberoMapper;
    private final BarberoValidation barberoValidation;

    @Override
    public void cambiarEstado(Long id) {
        Barbero barbero= getBarberoEntity(id);
        barbero.setActivo(!barbero.isActivo());
        barberoRepository.save(barbero);
    }

    @Override
    public BarberoResponse crear(BarberoRequest objeto) {

        barberoValidation.validarTelefonoCrear(objeto.getTelefono());

        Barbero barbero = barberoMapper.fromRequestToEntity(objeto);
        Barbero guardado = barberoRepository.save(barbero);

        return barberoMapper.toResponseDto(guardado);
    }

    @Override
    public BarberoResponse buscarPorId(Long id) {
        Barbero barbero = getBarberoEntity(id);

        return barberoMapper.toResponseDto(barbero);
    }

    public Barbero getBarberoEntity(Long id) {
        return barberoRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("No existe barbero con ese id"));
    }

    @Override
    public List<BarberoResponse> listar() {
        return barberoRepository.findAll()
                .stream()
                .map(barberoMapper::toResponseDto)
                .toList();
    }

    @Override
    public BarberoResponse actualizar(Long id, BarberoRequest objeto) {

        Barbero barbero = getBarberoEntity(id);

        barberoValidation.validarTelefonoActualizar(id, objeto.getTelefono());

        barbero.setNombre(objeto.getNombre());
        barbero.setApellido(objeto.getApellido());
        barbero.setTelefono(objeto.getTelefono());

        Barbero guardado = barberoRepository.save(barbero);
        return barberoMapper.toResponseDto(guardado);
    }
}
