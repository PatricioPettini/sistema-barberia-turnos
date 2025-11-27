package com.pato.service.interfaces;

import java.util.List;

public interface IGenericService<Req, Res> {
    Res crear(Req objeto);
    Res buscarPorId(Long id);
    List<Res> listar();
    Res actualizar(Long id, Req objeto);
}
