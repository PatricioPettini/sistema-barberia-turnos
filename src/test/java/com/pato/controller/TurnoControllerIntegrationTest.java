package com.pato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pato.config.TestMailConfig;
import com.pato.dto.request.TurnoRequest;
import com.pato.model.Barbero;
import com.pato.model.Servicio;
import com.pato.model.Turno;
import com.pato.model.enums.EstadoTurno;
import com.pato.repository.IBarberoRepository;
import com.pato.repository.IServicioRepository;
import com.pato.repository.ITurnoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Import(TestMailConfig.class)
class TurnoControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ITurnoRepository turnoRepository;

    @Autowired
    IBarberoRepository barberoRepository;

    @Autowired
    IServicioRepository servicioRepository;


    @BeforeEach
    void setup() {
        turnoRepository.deleteAll();
        barberoRepository.deleteAll();
        servicioRepository.deleteAll();

        Barbero barbero = new Barbero(null, "Luis", "Fernandez", "1199887766", true);
        barberoRepository.save(barbero);

        Servicio servicio = new Servicio(null, "Corte", 1500.0, 30, true);
        servicioRepository.save(servicio);

        Turno turno = Turno.builder()
                .nombreCliente("Juan")
                .telefonoCliente("1122334455")
                .emailCliente("juan@mail.com")
                .fecha(LocalDate.now())
                .horaDesde(LocalTime.of(14, 0))
                .horaHasta(LocalTime.of(14, 30))
                .barbero(barbero)
                .servicio(servicio)
                .estadoTurno(EstadoTurno.PENDIENTE)
                .activo(true)
                .recordatorioEnviado(false)
                .build();

        turnoRepository.save(turno);
    }

    @Test
    void crear_ok() throws Exception {
        Barbero b = barberoRepository.findAll().get(0);
        Servicio s = servicioRepository.findAll().get(0);

        TurnoRequest req = new TurnoRequest(
                "Pedro",
                "1199776655",
                "pedro@mail.com",
                LocalDate.now().plusDays(1),
                LocalTime.of(16, 0),
                b.getIdBarbero(),
                s.getIdServicio()
        );

        mockMvc.perform(post("/api/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCliente").value("Pedro"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editar_ok() throws Exception {
        Long id = turnoRepository.findAll().get(0).getIdTurno();

        TurnoRequest req = new TurnoRequest(
                "Carlos",
                "111222333",
                "carlos@mail.com",
                LocalDate.now().plusDays(1),
                LocalTime.of(15, 0),
                barberoRepository.findAll().get(0).getIdBarbero(),
                servicioRepository.findAll().get(0).getIdServicio()
        );

        mockMvc.perform(put("/api/turnos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCliente").value("Carlos"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listar_ok() throws Exception {
        mockMvc.perform(get("/api/turnos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreCliente").value("Juan"));
    }

    @Test
    void listarPorBarberoYFecha_ok() throws Exception {
        Long barberoId = barberoRepository.findAll().get(0).getIdBarbero();
        String fecha = LocalDate.now().toString();

        mockMvc.perform(get("/api/turnos/barbero/" + barberoId + "/" + fecha))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreCliente").value("Juan"));
    }

    @Test
    void listarPorFecha_ok() throws Exception {
        String fecha = LocalDate.now().toString();

        mockMvc.perform(get("/api/turnos/barbero/" + fecha))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreCliente").value("Juan"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void buscarPorId_ok() throws Exception {
        Long id = turnoRepository.findAll().get(0).getIdTurno();

        mockMvc.perform(get("/api/turnos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTurno").value(id));
    }

    @Test
    void cancelar_ok() throws Exception {
        Long id = turnoRepository.findAll().get(0).getIdTurno();

        mockMvc.perform(put("/api/turnos/" + id + "/cancelar"))
                .andExpect(status().isOk());
    }
}
