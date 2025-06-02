package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Administrador;
import com.hopematch.hopematch_backend.services.AdministradorService;
import com.hopematch.hopematch_backend.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdministradorControllerTest {

    @Mock
    private AdministradorService administradorService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AdministradorController administradorController;

    private Administrador admin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        admin = new Administrador();
        admin.setId(1);
        admin.setNombre("Admin Test");
        admin.setEmail("admin@test.com");
        admin.setContrasenia("hashedPassword");
    }

    @Test
    void postAdministrador_ShouldReturnSavedAdmin() {
        when(administradorService.saveAdministrador(any(Administrador.class))).thenReturn(admin);

        Administrador result = administradorController.postAdministrador(admin);

        assertEquals(admin, result);
        verify(administradorService, times(1)).saveAdministrador(any(Administrador.class));
    }

    @Test
    void login_WithValidCredentials_ShouldReturnToken() {
        when(administradorService.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));
        when(administradorService.verifyPassword("password123", "hashedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("admin@test.com", "administrador", 1)).thenReturn("mockToken");

        Administrador loginRequest = new Administrador();
        loginRequest.setEmail("admin@test.com");
        loginRequest.setContrasenia("password123");

        ResponseEntity<String> response = administradorController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("mockToken"));
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnUnauthorized() {
        when(administradorService.findByEmail("wrong@test.com")).thenReturn(Optional.empty());

        Administrador loginRequest = new Administrador();
        loginRequest.setEmail("wrong@test.com");
        loginRequest.setContrasenia("wrongpass");

        ResponseEntity<String> response = administradorController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Usuario o contraseña incorrectos", response.getBody());
    }

    @Test
    void getAllAdministradores_ShouldReturnAllAdmins() {
        List<Administrador> admins = Arrays.asList(admin, new Administrador());
        when(administradorService.getAllAdministradores()).thenReturn(admins);

        List<Administrador> result = administradorController.getAllAdministradores();

        assertEquals(2, result.size());
        verify(administradorService, times(1)).getAllAdministradores();
    }

    @Test
    void getAdministradorById_WithExistingId_ShouldReturnAdmin() {
        when(administradorService.getAdministradorById(1)).thenReturn(Optional.of(admin));

        ResponseEntity<Administrador> response = administradorController.getAdministradorById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(admin, response.getBody());
    }

    @Test
    void getAdministradorById_WithNonExistingId_ShouldReturnNotFound() {
        when(administradorService.getAdministradorById(999)).thenReturn(Optional.empty());

        ResponseEntity<Administrador> response = administradorController.getAdministradorById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateAdministrador_ShouldReturnUpdatedAdmin() {
        when(administradorService.updateAdministrador(1, admin)).thenReturn(admin);

        Administrador result = administradorController.updateAdministrador(1, admin);

        assertEquals(admin, result);
        verify(administradorService, times(1)).updateAdministrador(1, admin);
    }
}