package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Mensaje;
import com.hopematch.hopematch_backend.services.MensajeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MensajeControllerTest {

    @InjectMocks
    private MensajeController mensajeController;

    @Mock
    private MensajeService mensajeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearMensaje_SetsFechaYLeidoCorrectamente() {
        Mensaje inputMensaje = new Mensaje(1, 2, "Juan", "Maria", "Hola");
        inputMensaje.setLeido(false); // explícito

        Mensaje savedMensaje = new Mensaje(1, 2, "Juan", "Maria", "Hola");
        savedMensaje.setId(100);
        savedMensaje.setFecha(LocalDateTime.now());
        savedMensaje.setLeido(false);

        when(mensajeService.saveMensaje(any(Mensaje.class))).thenReturn(savedMensaje);

        Mensaje result = mensajeController.crearMensaje(inputMensaje);

        assertNotNull(result);
        assertEquals(savedMensaje.getId(), result.getId());
        assertFalse(result.isLeido());
        assertNotNull(result.getFecha());

        verify(mensajeService, times(1)).saveMensaje(any(Mensaje.class));
    }

    @Test
    void listarMensajes_ReturnsListOfMensajes() {
        List<Mensaje> mensajes = List.of(
                new Mensaje(1, 2, "A", "B", "Msg1"),
                new Mensaje(3, 4, "C", "D", "Msg2")
        );
        when(mensajeService.getAllMensajes()).thenReturn(mensajes);

        List<Mensaje> result = mensajeController.listarMensajes();

        assertEquals(2, result.size());
        verify(mensajeService, times(1)).getAllMensajes();
    }

    @Test
    void obtenerMensajePorId_Found_ReturnsMensaje() {
        Mensaje mensaje = new Mensaje(1, 2, "Juan", "Maria", "Hola");
        mensaje.setId(10);

        when(mensajeService.getMensajeById(10)).thenReturn(Optional.of(mensaje));

        ResponseEntity<Mensaje> response = mensajeController.obtenerMensajePorId(10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mensaje, response.getBody());
    }

    @Test
    void obtenerMensajePorId_NotFound_Returns404() {
        when(mensajeService.getMensajeById(999)).thenReturn(Optional.empty());

        ResponseEntity<Mensaje> response = mensajeController.obtenerMensajePorId(999);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void obtenerMensajesPorRemitente_ReturnsList() {
        List<Mensaje> mensajes = List.of(new Mensaje(1, 2, "Juan", "Maria", "Msg"));
        when(mensajeService.getMensajesByRemitente(1)).thenReturn(mensajes);

        List<Mensaje> result = mensajeController.obtenerMensajesPorRemitente(1);

        assertEquals(1, result.size());
        verify(mensajeService, times(1)).getMensajesByRemitente(1);
    }

    @Test
    void obtenerMensajesPorDestinatarioId_ReturnsList() {
        List<Mensaje> mensajes = List.of(new Mensaje(1, 2, "Juan", "Maria", "Msg"));
        when(mensajeService.getMensajesByDestinatario(2)).thenReturn(mensajes);

        List<Mensaje> result = mensajeController.obtenerMensajesPorDestinatarioId(2);

        assertEquals(1, result.size());
        verify(mensajeService, times(1)).getMensajesByDestinatario(2);
    }

    @Test
    void obtenerMensajesPorDestinatarioNombre_ReturnsList() {
        List<Mensaje> mensajes = List.of(new Mensaje(1, 2, "Juan", "Maria", "Msg"));
        when(mensajeService.getMensajesByDestinatarioNombre("Maria")).thenReturn(mensajes);

        List<Mensaje> result = mensajeController.obtenerMensajesPorDestinatario("Maria");

        assertEquals(1, result.size());
        verify(mensajeService, times(1)).getMensajesByDestinatarioNombre("Maria");
    }

    @Test
    void actualizarMensaje_ReturnsUpdatedMensaje() {
        Mensaje mensajeDetalles = new Mensaje(1, 2, "Juan", "Maria", "Nuevo mensaje");
        mensajeDetalles.setLeido(true);

        Mensaje mensajeActualizado = new Mensaje(1, 2, "Juan", "Maria", "Nuevo mensaje");
        mensajeActualizado.setId(5);
        mensajeActualizado.setLeido(true);

        when(mensajeService.updateMensaje(5, mensajeDetalles)).thenReturn(mensajeActualizado);

        ResponseEntity<Mensaje> response = mensajeController.actualizarMensaje(5, mensajeDetalles);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mensajeActualizado, response.getBody());
    }

    @Test
    void eliminarMensaje_VerifyDeleteCalled() {
        doNothing().when(mensajeService).deleteMensaje(3);

        ResponseEntity<Void> response = mensajeController.eliminarMensaje(3);

        assertEquals(204, response.getStatusCodeValue());
        verify(mensajeService, times(1)).deleteMensaje(3);
    }

    @Test
    void marcarMensajeComoLeido_Found_UpdatesAndReturnsMensaje() {
        Mensaje mensaje = new Mensaje(1, 2, "Juan", "Maria", "Hola");
        mensaje.setId(7);
        mensaje.setLeido(false);

        when(mensajeService.getMensajeById(7)).thenReturn(Optional.of(mensaje));
        when(mensajeService.saveMensaje(any(Mensaje.class))).thenReturn(mensaje);

        ResponseEntity<Mensaje> response = mensajeController.marcarMensajeComoLeido(7);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isLeido());
        verify(mensajeService, times(1)).saveMensaje(mensaje);
    }

    @Test
    void marcarMensajeComoLeido_NotFound_Returns404() {
        when(mensajeService.getMensajeById(999)).thenReturn(Optional.empty());

        ResponseEntity<Mensaje> response = mensajeController.marcarMensajeComoLeido(999);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}