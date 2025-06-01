package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Mensaje;
import com.hopematch.hopematch_backend.repositories.MensajeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MensajeServiceTest {

    @Mock
    private MensajeRepository mensajeRepository;

    @InjectMocks
    private MensajeService mensajeService;

    private Mensaje mensaje;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mensaje = new Mensaje(1, 2, "Remitente", "Destinatario", "Contenido del mensaje");
        mensaje.setId(1);
        mensaje.setLeido(false);
        mensaje.setFecha(LocalDateTime.now());
    }

    @Test
    @DisplayName("Guardar mensaje - Éxito")
    void saveMensaje_Success() {
        // Arrange
        when(mensajeRepository.save(any(Mensaje.class))).thenReturn(mensaje);

        // Act
        Mensaje savedMensaje = mensajeService.saveMensaje(mensaje);

        // Assert
        assertNotNull(savedMensaje);
        assertEquals("Contenido del mensaje", savedMensaje.getMensaje());
        assertFalse(savedMensaje.isLeido());
        verify(mensajeRepository, times(1)).save(mensaje);
    }

    @Test
    @DisplayName("Obtener todos los mensajes")
    void getAllMensajes_ReturnsAll() {
        // Arrange
        when(mensajeRepository.findAll()).thenReturn(Arrays.asList(mensaje, new Mensaje()));

        // Act
        List<Mensaje> mensajes = mensajeService.getAllMensajes();

        // Assert
        assertEquals(2, mensajes.size());
        verify(mensajeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener mensaje por ID existente")
    void getMensajeById_ExistingId_ReturnsMensaje() {
        // Arrange
        when(mensajeRepository.findById(1)).thenReturn(Optional.of(mensaje));

        // Act
        Optional<Mensaje> foundMensaje = mensajeService.getMensajeById(1);

        // Assert
        assertTrue(foundMensaje.isPresent());
        assertEquals("Remitente", foundMensaje.get().getRemitente());
        verify(mensajeRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Obtener mensaje por ID inexistente")
    void getMensajeById_NonExistingId_ReturnsEmpty() {
        // Arrange
        when(mensajeRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<Mensaje> foundMensaje = mensajeService.getMensajeById(999);

        // Assert
        assertTrue(foundMensaje.isEmpty());
        verify(mensajeRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Obtener mensajes por remitente")
    void getMensajesByRemitente_ValidId_ReturnsMensajes() {
        // Arrange
        when(mensajeRepository.findByIdRemitente(1)).thenReturn(Arrays.asList(mensaje));

        // Act
        List<Mensaje> mensajes = mensajeService.getMensajesByRemitente(1);

        // Assert
        assertEquals(1, mensajes.size());
        verify(mensajeRepository, times(1)).findByIdRemitente(1);
    }

    @Test
    @DisplayName("Obtener mensajes por destinatario (ID)")
    void getMensajesByDestinatario_ValidId_ReturnsMensajes() {
        when(mensajeRepository.findByIdDestinatario(2)).thenReturn(Arrays.asList(mensaje));

        List<Mensaje> mensajes = mensajeService.getMensajesByDestinatario(2);

        assertEquals(1, mensajes.size());
        verify(mensajeRepository, times(1)).findByIdDestinatario(2);
    }

    @Test
    @DisplayName("Obtener mensajes por nombre de destinatario")
    void getMensajesByDestinatarioNombre_ValidName_ReturnsMensajes() {
        when(mensajeRepository.findByDestinatario("Destinatario")).thenReturn(Arrays.asList(mensaje));

        List<Mensaje> mensajes = mensajeService.getMensajesByDestinatarioNombre("Destinatario");

        assertEquals(1, mensajes.size());
        verify(mensajeRepository, times(1)).findByDestinatario("Destinatario");
    }

    @Test
    @DisplayName("Actualizar mensaje existente")
    void updateMensaje_ExistingId_Success() {
        Mensaje updatedDetails = new Mensaje();
        updatedDetails.setMensaje("Mensaje actualizado");
        updatedDetails.setLeido(true);

        when(mensajeRepository.findById(1)).thenReturn(Optional.of(mensaje));
        when(mensajeRepository.save(any(Mensaje.class))).thenReturn(updatedDetails);

        Mensaje result = mensajeService.updateMensaje(1, updatedDetails);

        assertNotNull(result);
        assertEquals("Mensaje actualizado", result.getMensaje());
        assertTrue(result.isLeido());
        verify(mensajeRepository, times(1)).findById(1);
        verify(mensajeRepository, times(1)).save(any(Mensaje.class));
    }

    @Test
    @DisplayName("Actualizar mensaje inexistente - Lanza excepción")
    void updateMensaje_NonExistingId_ThrowsException() {
        Mensaje updatedDetails = new Mensaje();
        when(mensajeRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> mensajeService.updateMensaje(999, updatedDetails));
        verify(mensajeRepository, times(1)).findById(999);
        verify(mensajeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Eliminar mensaje")
    void deleteMensaje_Success() {
        doNothing().when(mensajeRepository).deleteById(1);

        mensajeService.deleteMensaje(1);

        verify(mensajeRepository, times(1)).deleteById(1);
    }
}
