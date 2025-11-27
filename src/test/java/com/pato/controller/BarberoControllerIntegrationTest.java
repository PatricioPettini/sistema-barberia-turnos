package com.pato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pato.dto.request.BarberoRequest;
import com.pato.model.Barbero;
import com.pato.repository.IBarberoRepository;
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
class BarberoControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    IBarberoRepository barberoRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        barberoRepository.deleteAll();
        barberoRepository.save(new Barbero(null, "Luis", "Fernandez", "1199887766", true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crear_ok() throws Exception {
        BarberoRequest req = new BarberoRequest("Juan", "Perez", "1122334455");

        mockMvc.perform(post("/api/barberos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void listar_ok() throws Exception {
        mockMvc.perform(get("/api/barberos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Luis"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void buscarPorId_ok() throws Exception {
        Long id = barberoRepository.findAll().get(0).getIdBarbero();

        mockMvc.perform(get("/api/barberos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idBarbero").value(id));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void actualizar_ok() throws Exception {
        Long id = barberoRepository.findAll().get(0).getIdBarbero();

        BarberoRequest req = new BarberoRequest("Pedro", "Garcia", "1122112211");

        mockMvc.perform(put("/api/barberos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pedro"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cambiarEstado_ok() throws Exception {
        Long id = barberoRepository.findAll().get(0).getIdBarbero();

        mockMvc.perform(put("/api/barberos/" + id + "/estado"))
                .andExpect(status().isNoContent());
    }
}
