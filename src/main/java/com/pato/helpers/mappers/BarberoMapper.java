package com.pato.helpers.mappers;

import com.pato.dto.request.BarberoRequest;
import com.pato.dto.response.BarberoResponse;
import com.pato.model.Barbero;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BarberoMapper implements IMapper<Barbero, BarberoResponse, BarberoRequest>{

    private final ModelMapper modelMapper;


    @Override
    public BarberoResponse toResponseDto(Barbero entity) {
        return modelMapper.map(entity, BarberoResponse.class);
    }

    @Override
    public Barbero fromRequestToEntity(BarberoRequest request) {
        return modelMapper.map(request, Barbero.class);
    }
}
