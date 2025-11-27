package com.pato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pato.dto.request.BarberoRequest;
import com.pato.dto.response.BarberoResponse;
import com.pato.service.interfaces.IBarberoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BarberoController.class)
@AutoConfigureMockMvc(addFilters = false)
class BarberoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBarberoService barberoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void crear_ok() throws Exception {
        BarberoRequest req = new BarberoRequest("Juan", "Gomez", "1122334455");
        BarberoResponse res = new BarberoResponse(1L, "Juan", "Gomez", "1122334455", true);

        Mockito.when(barberoService.crear(any())).thenReturn(res);

        mockMvc.perform(post("/api/barberos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idBarbero").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crear_errorValidacion() throws Exception {
        BarberoRequest req = new BarberoRequest("", "", "123");

        mockMvc.perform(post("/api/barberos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void listar_ok() throws Exception {
        Mockito.when(barberoService.listar()).thenReturn(List.of(
                new BarberoResponse(1L, "Pepe", "Diaz", "1122334455", true)
        ));

        mockMvc.perform(get("/api/barberos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idBarbero").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void buscarPorId_ok() throws Exception {
        BarberoResponse res = new BarberoResponse(5L, "Juan", "Lopez", "1199887766", true);

        Mockito.when(barberoService.buscarPorId(5L)).thenReturn(res);

        mockMvc.perform(get("/api/barberos/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idBarbero").value(5L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void actualizar_ok() throws Exception {
        BarberoRequest req = new BarberoRequest("Carlos", "Sanchez", "1122445588");
        BarberoResponse res = new BarberoResponse(3L, "Carlos", "Sanchez", "1122445588", true);

        Mockito.when(barberoService.actualizar(eq(3L), any())).thenReturn(res);

        mockMvc.perform(put("/api/barberos/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idBarbero").value(3L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cambiarEstado_ok() throws Exception {
        mockMvc.perform(put("/api/barberos/10/estado"))
                .andExpect(status().isNoContent());

        Mockito.verify(barberoService).cambiarEstado(10L);
    }
}
