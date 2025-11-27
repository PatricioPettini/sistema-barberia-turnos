package com.pato.service;

import com.pato.model.Role;
import com.pato.repository.IRoleRepository;
import com.pato.service.implementations.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private IRoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ok() {
        Role r1 = new Role(1L, "ADMIN", new HashSet<>());
        Role r2 = new Role(2L, "USER", new HashSet<>());

        when(roleRepository.findAll()).thenReturn(List.of(r1, r2));

        List<Role> lista = roleService.findAll();

        assertEquals(2, lista.size());
        assertEquals("ADMIN", lista.get(0).getName());
        assertEquals("USER", lista.get(1).getName());
    }

    @Test
    void findById_ok() {
        Role role = new Role(1L, "ADMIN", new HashSet<>());

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        Optional<Role> result = roleService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("ADMIN", result.get().getName());
    }

    @Test
    void findById_empty() {
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Role> result = roleService.findById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void save_ok() {
        Role role = new Role(1L, "ADMIN", new HashSet<>());

        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.save(role);

        verify(roleRepository, times(1)).save(role);
        assertEquals("ADMIN", result.getName());
    }

    @Test
    void deleteById_ok() {
        doNothing().when(roleRepository).deleteById(1L);

        roleService.deleteById(1L);

        verify(roleRepository, times(1)).deleteById(1L);
    }

    @Test
    void update_ok() {
        Role role = new Role(1L, "ADMIN", new HashSet<>());

        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.update(role);

        verify(roleRepository, times(1)).save(role);
        assertEquals("ADMIN", result.getName());
    }
}
