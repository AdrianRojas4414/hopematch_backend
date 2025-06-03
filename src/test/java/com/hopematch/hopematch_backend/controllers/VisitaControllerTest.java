package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Visita;
import com.hopematch.hopematch_backend.services.VisitaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VisitaControllerTest {

    @InjectMocks
    private VisitaController visitaController;

    @Mock
    private VisitaService visitaService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Agendar visita exitosamente con datos válidos")
    void testAgendarVisitaSuccess() {
        Map<String, Object> request = new HashMap<>();
        request.put("fechaVisita", LocalDate.now().plusDays(1).toString());
        request.put("horaVisita", "10:30");
        request.put("padrinoId", 123L);
        request.put("encargadoId", 45);

        Visita visitaMock = new Visita();
        visitaMock.setId(1);
        visitaMock.setFechaVisita(LocalDate.parse(request.get("fechaVisita").toString()));
        visitaMock.setHoraVisita(LocalTime.parse(request.get("horaVisita").toString()));
        visitaMock.setPadrinoId(123L);
        visitaMock.setEncargadoId(45);
        visitaMock.setEstadoVisita(Visita.EstadoVisita.EN_REVISION);

        when(visitaService.saveVisita(any(Visita.class))).thenReturn(visitaMock);

        ResponseEntity<?> response = visitaController.agendarVisita(request);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Visita);
        Visita body = (Visita) response.getBody();
        assertEquals(1, body.getId());
        assertEquals(Visita.EstadoVisita.EN_REVISION, body.getEstadoVisita());
        verify(visitaService, times(1)).saveVisita(any(Visita.class));
    }

    @Test
    @DisplayName("Error al agendar visita con fecha inválida")
    void testAgendarVisitaFailFechaInvalida() {
        Map<String, Object> request = new HashMap<>();
        request.put("fechaVisita", "2020-01-01");  // Fecha pasada
        request.put("horaVisita", "10:30");
        request.put("padrinoId", 123L);
        request.put("encargadoId", 45);

        when(visitaService.saveVisita(any(Visita.class))).thenThrow(new RuntimeException("La fecha de visita no puede ser anterior a hoy"));

        ResponseEntity<?> response = visitaController.agendarVisita(request);

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("La fecha de visita no puede ser anterior a hoy"));
        verify(visitaService, times(1)).saveVisita(any(Visita.class));
    }

    @Test
    @DisplayName("Listar todas las visitas exitosamente")
    void testListarVisitas() {
        List<Visita> listaVisitas = new ArrayList<>();
        listaVisitas.add(new Visita(1, 123L, 45, LocalDate.now().plusDays(1), LocalTime.of(10, 0), Visita.EstadoVisita.EN_REVISION));
        listaVisitas.add(new Visita(2, 124L, 46, LocalDate.now().plusDays(2), LocalTime.of(11, 0), Visita.EstadoVisita.ACEPTADA));

        when(visitaService.getAllVisitas()).thenReturn(listaVisitas);

        ResponseEntity<List<Visita>> response = visitaController.listarVisitas();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(visitaService, times(1)).getAllVisitas();
    }

    @Test
    @DisplayName("Listar visitas por padrino")
    void testListarVisitasPorPadrino() {
        Long padrinoId = 123L;
        List<Visita> listaVisitas = Collections.singletonList(
                new Visita(1, padrinoId, 45, LocalDate.now().plusDays(1), LocalTime.of(10, 0), Visita.EstadoVisita.EN_REVISION));

        when(visitaService.getVisitasByPadrino(padrinoId)).thenReturn(listaVisitas);

        ResponseEntity<List<Visita>> response = visitaController.listarVisitasPorPadrino(padrinoId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(padrinoId, response.getBody().get(0).getPadrinoId());
        verify(visitaService, times(1)).getVisitasByPadrino(padrinoId);
    }

    @Test
    @DisplayName("Listar visitas por encargado")
    void testListarVisitasPorEncargado() {
        Integer encargadoId = 45;
        List<Visita> listaVisitas = Collections.singletonList(
                new Visita(1, 123L, encargadoId, LocalDate.now().plusDays(1), LocalTime.of(10, 0), Visita.EstadoVisita.EN_REVISION));

        when(visitaService.getVisitasByEncargado(encargadoId)).thenReturn(listaVisitas);

        ResponseEntity<List<Visita>> response = visitaController.listarVisitasPorEncargado(encargadoId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(encargadoId, response.getBody().get(0).getEncargadoId());
        verify(visitaService, times(1)).getVisitasByEncargado(encargadoId);
    }

    @Test
    @DisplayName("Obtener visita por ID existente")
    void testObtenerVisitaPorId() {
        Integer id = 1;
        Visita visita = new Visita(id, 123L, 45, LocalDate.now().plusDays(1), LocalTime.of(10, 0), Visita.EstadoVisita.EN_REVISION);

        when(visitaService.getVisitaById(id)).thenReturn(visita);

        ResponseEntity<Visita> response = visitaController.obtenerVisitaPorId(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(id, response.getBody().getId());
        verify(visitaService, times(1)).getVisitaById(id);
    }

    @Test
    @DisplayName("Actualizar estado de visita exitosamente")
    void testActualizarEstadoVisita() {
        Integer id = 1;
        Map<String, String> request = new HashMap<>();
        request.put("estado", "ACEPTADA");

        Visita visitaActualizada = new Visita(id, 123L, 45, LocalDate.now().plusDays(1), LocalTime.of(10, 0), Visita.EstadoVisita.ACEPTADA);

        when(visitaService.updateEstadoVisita(id, Visita.EstadoVisita.ACEPTADA)).thenReturn(visitaActualizada);

        ResponseEntity<Visita> response = visitaController.actualizarEstadoVisita(id, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Visita.EstadoVisita.ACEPTADA, response.getBody().getEstadoVisita());
        verify(visitaService, times(1)).updateEstadoVisita(id, Visita.EstadoVisita.ACEPTADA);
    }
}