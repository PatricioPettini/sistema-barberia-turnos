package com.pato.service;

import com.pato.dto.request.AuthLoginRequestDTO;
import com.pato.dto.response.AuthResponseDTO;
import com.pato.model.Permission;
import com.pato.model.Role;
import com.pato.model.UserSec;
import com.pato.repository.IUserRepository;
import com.pato.service.implementations.UserDetailsServiceImp;
import com.pato.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImpTest {

    @Mock
    private IUserRepository userRepo;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserDetailsServiceImp service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private UserSec createUser() {
        Permission p1 = new Permission(1L, "TURNOS_CREAR");
        Permission p2 = new Permission(2L, "TURNOS_EDITAR");

        Role role = new Role(1L, "ADMIN", Set.of(p1, p2));

        return new UserSec(
                1L,
                "pato",
                "1234",
                true, true, true, true,
                Set.of(role)
        );
    }

    @Test
    void loadUserByUsername_ok() {
        UserSec user = createUser();
        when(userRepo.findByUsername("pato")).thenReturn(Optional.of(user));

        UserDetails details = service.loadUserByUsername("pato");

        assertEquals("pato", details.getUsername());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("TURNOS_CREAR")));
    }

    @Test
    void loadUserByUsername_noExiste() {
        when(userRepo.findByUsername("xxx")).thenReturn(Optional.empty());

        assertThrows(Exception.class,
                () -> service.loadUserByUsername("xxx"));
    }

    @Test
    void authenticate_ok() {
        UserSec user = createUser();
        when(userRepo.findByUsername("pato")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("1234", "1234")).thenReturn(true);

        var auth = service.authenticate("pato", "1234");

        assertEquals("pato", auth.getPrincipal());
    }

    @Test
    void authenticate_usuarioInvalido() {
        when(userRepo.findByUsername("noexiste")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.authenticate("noexiste", "1234"));
    }

    @Test
    void authenticate_passwordIncorrecta() {
        UserSec user = createUser();
        when(userRepo.findByUsername("pato")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "1234")).thenReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> service.authenticate("pato", "wrong"));
    }

    @Test
    void loginUser_ok() {
        AuthLoginRequestDTO req = new AuthLoginRequestDTO("pato", "1234");
        UserSec user = createUser();

        when(userRepo.findByUsername("pato")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("1234", "1234")).thenReturn(true);
        when(jwtUtils.createToken(any())).thenReturn("JWT-FAKE");

        AuthResponseDTO response = service.loginUser(req);

        assertEquals("pato", response.username());
        assertEquals("login ok", response.message());
        assertEquals("JWT-FAKE", response.jwt());
        assertTrue(response.status());
    }
}
