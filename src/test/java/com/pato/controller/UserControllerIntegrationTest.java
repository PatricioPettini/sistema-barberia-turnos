package com.pato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pato.model.Role;
import com.pato.model.UserSec;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "JWT_KEY=test-jwt-key"
})
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_ok() throws Exception {

        Role role = new Role();
        role.setId(1L);

        UserSec user = new UserSec();
        user.setUsername("pato");
        user.setPassword("12345");
        user.setRolesList(Set.of(role));

        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("pato"))
                .andExpect(jsonPath("$.rolesList[0].name").value("ADMIN"));
    }

    @Test
    void createUser_rolesNoExisten_badRequest() throws Exception {

        Role role = new Role();
        role.setId(99L);

        UserSec user = new UserSec();
        user.setUsername("pato");
        user.setPassword("12345");
        user.setRolesList(Set.of(role));

        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Los roles enviados no existen."));
    }
}
