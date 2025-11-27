package com.pato.service;

import com.pato.model.UserSec;
import com.pato.repository.IUserRepository;
import com.pato.service.implementations.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserSec user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new UserSec();
        user.setId(1L);
        user.setUsername("pato");
        user.setPassword("1234");
    }

    @Test
    void findAll_ok() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserSec> result = userService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void findById_ok() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserSec> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("pato", result.get().getUsername());
        verify(userRepository).findById(1L);
    }

    @Test
    void save_ok() {
        when(userRepository.save(user)).thenReturn(user);

        UserSec result = userService.save(user);

        assertNotNull(result);
        assertEquals("pato", result.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    void deleteById_ok() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteById(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void update_ok() {
        when(userRepository.save(user)).thenReturn(user);

        userService.update(user);

        verify(userRepository).save(user);
    }

    @Test
    void encriptPassword_ok() {
        String pass = "admin123";

        String encrypted = userService.encriptPassword(pass);

        assertNotNull(encrypted);
        assertNotEquals(pass, encrypted);
        assertTrue(encrypted.startsWith("$2"));
    }

    @Test
    void findByUsername_ok() {
        when(userRepository.findByUsername("pato")).thenReturn(Optional.of(user));

        UserSec result = userService.findByUsername("pato");

        assertNotNull(result);
        assertEquals("pato", result.getUsername());
        verify(userRepository).findByUsername("pato");
    }

    @Test
    void findByUsername_error() {
        when(userRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.findByUsername("noexiste"));
    }
}
