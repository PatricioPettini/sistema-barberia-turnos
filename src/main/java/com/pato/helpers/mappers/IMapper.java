package com.pato.helpers.mappers;

public interface IMapper<Ent, Res, Req> {

    Res toResponseDto(Ent entity);

    Ent fromRequestToEntity(Req request);
}
