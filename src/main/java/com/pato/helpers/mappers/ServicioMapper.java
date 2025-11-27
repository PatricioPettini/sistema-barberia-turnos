package com.pato.helpers.mappers;

import com.pato.dto.request.ServicioRequest;
import com.pato.dto.response.ServicioResponse;
import com.pato.model.Servicio;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServicioMapper  implements IMapper<Servicio, ServicioResponse, ServicioRequest>{

    private final ModelMapper modelMapper;


    @Override
    public ServicioResponse toResponseDto(Servicio entity) {
        return modelMapper.map(entity, ServicioResponse.class);
    }

    @Override
    public Servicio fromRequestToEntity(ServicioRequest request) {
        return modelMapper.map(request, Servicio.class);
    }
}
