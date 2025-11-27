package com.pato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pato.dto.request.AuthLoginRequestDTO;
import com.pato.dto.response.AuthResponseDTO;
import com.pato.service.implementations.UserDetailsServiceImp;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsServiceImp userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void hola_ok() throws Exception {
        mockMvc.perform(get("/api/auth/hola"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hola Mundo"));
    }

    @Test
    void login_ok() throws Exception {

        AuthLoginRequestDTO request = new AuthLoginRequestDTO(
                "test@mail.com",
                "12345"
        );

        AuthResponseDTO response = new AuthResponseDTO(
                "test@mail.com",
                "Login exitoso",
                "jwt-token-123",
                true
        );

        Mockito.when(userDetailsService.loginUser(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("jwt-token-123"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.username").value("test@mail.com"));
    }
}
