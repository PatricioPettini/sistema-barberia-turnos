package com.pato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pato.dto.request.TurnoRequest;
import com.pato.dto.response.TurnoResponse;
import com.pato.service.interfaces.ITurnoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TurnoController.class)
@AutoConfigureMockMvc(addFilters = false)
class TurnoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ITurnoService turnoService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void crear_ok() throws Exception {

        TurnoRequest req = new TurnoRequest(
                "Juan",
                "1122334455",
                "juan@mail.com",
                LocalDate.now(),
                LocalTime.of(14, 0),
                1L,
                1L
        );

        TurnoResponse res = new TurnoResponse();
        res.setIdTurno(10L);
        res.setNombreCliente("Juan");

        Mockito.when(turnoService.crear(any(TurnoRequest.class)))
                .thenReturn(res);

        mockMvc.perform(post("/api/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTurno").value(10L))
                .andExpect(jsonPath("$.nombreCliente").value("Juan"));
    }

    @Test
    void crear_errorValidacion() throws Exception {

        TurnoRequest req = new TurnoRequest(
                "",
                "123",
                "mailMalo",
                LocalDate.now(),
                null,
                null,
                null
        );

        mockMvc.perform(post("/api/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editar_ok() throws Exception {

        TurnoRequest req = new TurnoRequest(
                "Pedro",
                "1199887766",
                "pedro@mail.com",
                LocalDate.now(),
                LocalTime.of(16, 0),
                1L,
                1L
        );

        TurnoResponse res = new TurnoResponse();
        res.setIdTurno(1L);
        res.setNombreCliente("Pedro");

        Mockito.when(turnoService.actualizar(eq(1L), any(TurnoRequest.class)))
                .thenReturn(res);

        mockMvc.perform(put("/api/turnos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCliente").value("Pedro"));
    }

    @Test
    void listar_ok() throws Exception {

        TurnoResponse t = new TurnoResponse();
        t.setIdTurno(5L);
        t.setNombreCliente("Juan");

        Mockito.when(turnoService.listar())
                .thenReturn(List.of(t));

        mockMvc.perform(get("/api/turnos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTurno").value(5L));
    }

    @Test
    void listarPorBarberoYFecha_ok() throws Exception {

        Long idBarbero = 2L;
        LocalDate fecha = LocalDate.now();

        TurnoResponse t = new TurnoResponse();
        t.setIdTurno(3L);

        Mockito.when(turnoService.listarPorBarberoYFecha(idBarbero, fecha))
                .thenReturn(List.of(t));

        mockMvc.perform(get("/api/turnos/barbero/" + idBarbero + "/" + fecha))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTurno").value(3L));
    }

    @Test
    void listarPorFecha_ok() throws Exception {

        LocalDate fecha = LocalDate.now();

        TurnoResponse t = new TurnoResponse();
        t.setIdTurno(7L);

        Mockito.when(turnoService.listarPorFecha(fecha))
                .thenReturn(List.of(t));

        mockMvc.perform(get("/api/turnos/barbero/" + fecha))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTurno").value(7L));
    }

    @Test
    void buscarPorId_ok() throws Exception {

        TurnoResponse t = new TurnoResponse();
        t.setIdTurno(99L);

        Mockito.when(turnoService.buscarPorId(99L))
                .thenReturn(t);

        mockMvc.perform(get("/api/turnos/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTurno").value(99L));
    }

    @Test
    void cancelar_ok() throws Exception {

        TurnoResponse t = new TurnoResponse();
        t.setIdTurno(5L);

        Mockito.when(turnoService.cancelar(5L))
                .thenReturn(t);

        mockMvc.perform(put("/api/turnos/5/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTurno").value(5L));
    }
}
