package com.pato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pato.model.Role;
import com.pato.model.UserSec;
import com.pato.service.interfaces.IRoleService;
import com.pato.service.interfaces.IUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    @MockBean
    private IRoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_ok() throws Exception {

        Role roleInput = new Role();
        roleInput.setId(1L);

        UserSec inputUser = new UserSec();
        inputUser.setUsername("pato");
        inputUser.setPassword("12345");
        inputUser.setRolesList(Set.of(roleInput));

        Mockito.when(userService.encriptPassword("12345")).thenReturn("ENCRIPTADA");

        Role roleDB = new Role();
        roleDB.setId(1L);
        roleDB.setName("ADMIN");

        Mockito.when(roleService.findById(1L))
                .thenReturn(Optional.of(roleDB));

        UserSec savedUser = new UserSec();
        savedUser.setId(100L);
        savedUser.setUsername("pato");
        savedUser.setPassword("ENCRIPTADA");
        savedUser.setRolesList(Set.of(roleDB));

        Mockito.when(userService.save(any(UserSec.class)))
                .thenReturn(savedUser);

        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inputUser))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.username").value("pato"))
                .andExpect(jsonPath("$.password").value("ENCRIPTADA"))
                .andExpect(jsonPath("$.rolesList[0].name").value("ADMIN"));
    }

    @Test
    void createUser_rolesNoExisten_badRequest() throws Exception {

        Role roleInput = new Role();
        roleInput.setId(99L);

        UserSec inputUser = new UserSec();
        inputUser.setUsername("pato");
        inputUser.setPassword("12345");
        inputUser.setRolesList(Set.of(roleInput));

        Mockito.when(userService.encriptPassword("12345"))
                .thenReturn("ENCRIPTADA");

        Mockito.when(roleService.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inputUser))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Los roles enviados no existen."));
    }
}
