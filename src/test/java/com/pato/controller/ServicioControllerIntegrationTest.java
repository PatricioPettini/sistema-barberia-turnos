package com.pato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pato.dto.request.ServicioRequest;
import com.pato.model.Servicio;
import com.pato.repository.IServicioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ServicioControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    IServicioRepository servicioRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        servicioRepository.deleteAll();
        servicioRepository.save(
                new Servicio(null, "Corte básico", 1500.0, 30, true)
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crear_ok() throws Exception {
        ServicioRequest req = new ServicioRequest(
                "Barba completa",
                2000.0,
                45
        );

        mockMvc.perform(post("/api/servicios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreServicio").value("Barba completa"))
                .andExpect(jsonPath("$.precio").value(2000.0))
                .andExpect(jsonPath("$.duracionMinutos").value(45));
    }

    @Test
    void listar_ok() throws Exception {
        mockMvc.perform(get("/api/servicios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreServicio").value("Corte básico"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void buscarPorId_ok() throws Exception {
        Long id = servicioRepository.findAll().get(0).getIdServicio();

        mockMvc.perform(get("/api/servicios/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idServicio").value(id));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void actualizar_ok() throws Exception {
        Long id = servicioRepository.findAll().get(0).getIdServicio();

        ServicioRequest req = new ServicioRequest(
                "Corte premium",
                3000.0,
                50
        );

        mockMvc.perform(put("/api/servicios/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreServicio").value("Corte premium"))
                .andExpect(jsonPath("$.precio").value(3000.0))
                .andExpect(jsonPath("$.duracionMinutos").value(50));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cambiarEstado_ok() throws Exception {
        Long id = servicioRepository.findAll().get(0).getIdServicio();

        mockMvc.perform(put("/api/servicios/" + id + "/estado"))
                .andExpect(status().isNoContent());
    }
}
