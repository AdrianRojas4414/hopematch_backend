package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Padrino;
import com.hopematch.hopematch_backend.services.PadrinoService;
import com.hopematch.hopematch_backend.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PadrinoControllerTest {

    @Mock
    private PadrinoService padrinoService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private PadrinoController padrinoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPostPadrino() {
        Padrino padrino = new Padrino();
        padrino.setNombre("Test");
        when(padrinoService.savePadrino(any())).thenReturn(padrino);

        Padrino response = padrinoController.postPadrino(padrino);
        assertEquals("Test", response.getNombre());
        verify(padrinoService).savePadrino(padrino);
    }

    @Test
    void testLoginSuccess_NormalState() {
        Padrino login = new Padrino();
        login.setEmail("email@test.com");
        login.setContrasenia("plainPass");

        Padrino stored = new Padrino();
        stored.setEmail("email@test.com");
        stored.setContrasenia("$2a$10$7QGQhN1f.yFzxlvG68i6JutP3UL1M8hpWWVkEqpqqyqZIl7SFF0T."); // bcrypt hash for "plainPass"
        stored.setEstado("Activo");
        stored.setId(5);

        when(padrinoService.findByEmail("email@test.com")).thenReturn(Optional.of(stored));
        when(padrinoService.verifyPassword("plainPass", stored.getContrasenia())).thenReturn(true);
        when(jwtUtil.generateToken(stored.getEmail(), "padrino", stored.getId())).thenReturn("token123");

        ResponseEntity<String> resp = padrinoController.login(login);
        assertEquals(200, resp.getStatusCodeValue());
        assertTrue(resp.getBody().contains("token123"));
    }

    @Test
    void testLoginSuccess_EstadoEnRevision() {
        Padrino login = new Padrino();
        login.setEmail("email@test.com");
        login.setContrasenia("plainPass");

        Padrino stored = new Padrino();
        stored.setEmail("email@test.com");
        stored.setContrasenia("hashed");
        stored.setEstado("En revision");
        stored.setId(1);

        when(padrinoService.findByEmail(any())).thenReturn(Optional.of(stored));
        when(padrinoService.verifyPassword(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString(), anyInt())).thenReturn("tokenRev");

        ResponseEntity<String> resp = padrinoController.login(login);
        assertEquals(200, resp.getStatusCodeValue());
        assertTrue(resp.getBody().contains("Su cuenta está en revisión"));
        assertTrue(resp.getBody().contains("tokenRev"));
    }

    @Test
    void testLoginSuccess_EstadoSuspendido() {
        Padrino login = new Padrino();
        login.setEmail("email@test.com");
        login.setContrasenia("plainPass");

        Padrino stored = new Padrino();
        stored.setEmail("email@test.com");
        stored.setContrasenia("hashed");
        stored.setEstado("Suspendido");
        stored.setId(1);

        when(padrinoService.findByEmail(any())).thenReturn(Optional.of(stored));
        when(padrinoService.verifyPassword(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString(), anyInt())).thenReturn("tokenSusp");

        ResponseEntity<String> resp = padrinoController.login(login);
        assertEquals(200, resp.getStatusCodeValue());
        assertTrue(resp.getBody().contains("Su cuenta ha sido suspendida"));
        assertTrue(resp.getBody().contains("tokenSusp"));
    }

    @Test
    void testLoginFailure() {
        Padrino login = new Padrino();
        login.setEmail("email@test.com");
        login.setContrasenia("wrongPass");

        when(padrinoService.findByEmail(any())).thenReturn(Optional.empty());

        ResponseEntity<String> resp = padrinoController.login(login);
        assertEquals(401, resp.getStatusCodeValue());
        assertEquals("Usuario o contraseña incorrectos", resp.getBody());
    }

    @Test
    void testHelloWorld() {
        ResponseEntity<String> resp = padrinoController.helloWorld();
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals("Hello World! Esto es HOPEMATCH", resp.getBody());
    }

    @Test
    void testGetAllPadrinos() {
        when(padrinoService.getAllPadrinos()).thenReturn(List.of(new Padrino(), new Padrino()));

        List<Padrino> padrinos = padrinoController.getAllPadrinos();
        assertEquals(2, padrinos.size());
    }

    @Test
    void testGetPadrinoById_Success() {
        Padrino padrino = new Padrino();
        padrino.setNombre("Nombre");
        when(padrinoService.getPadrinoById(1)).thenReturn(Optional.of(padrino));

        ResponseEntity<Padrino> resp = padrinoController.getPadrinoById(1);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals("Nombre", resp.getBody().getNombre());
    }

    @Test
    void testUpdatePadrino() {
        Padrino updated = new Padrino();
        updated.setNombre("Actualizado");

        when(padrinoService.updatePadrino(eq(1), any())).thenReturn(updated);

        Padrino result = padrinoController.updatePadrino(1, updated);
        assertEquals("Actualizado", result.getNombre());
    }
}