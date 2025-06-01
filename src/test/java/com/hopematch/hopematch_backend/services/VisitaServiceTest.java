package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Visita;
import com.hopematch.hopematch_backend.repositories.VisitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VisitaServiceTest {

    @Mock
    private VisitaRepository visitaRepository;

    @InjectMocks
    private VisitaService visitaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Guardar visita válida")
    void saveVisita_ValidVisita_ReturnsSavedVisita() {
        Visita visita = new Visita();
        visita.setPadrinoId(1L);
        visita.setEncargadoId(1);
        visita.setFechaVisita(LocalDate.now().plusDays(1));
        visita.setHoraVisita(LocalTime.of(10, 0));

        when(visitaRepository.save(any(Visita.class))).thenReturn(visita);

        Visita savedVisita = visitaService.saveVisita(visita);

        assertNotNull(savedVisita);
        assertEquals(Visita.EstadoVisita.EN_REVISION, savedVisita.getEstadoVisita());
        verify(visitaRepository, times(1)).save(visita);
    }

    @Test
    @DisplayName("Guardar visita con fecha anterior a hoy - Debe lanzar excepción")
    void saveVisita_PastDate_ThrowsException() {
        Visita visita = new Visita();
        visita.setPadrinoId(1L);
        visita.setEncargadoId(1);
        visita.setFechaVisita(LocalDate.now().minusDays(1));
        visita.setHoraVisita(LocalTime.of(10, 0));

        assertThrows(RuntimeException.class, () -> visitaService.saveVisita(visita));
        verify(visitaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Guardar visita sin hora - Debe lanzar excepción")
    void saveVisita_NoTime_ThrowsException() {
        Visita visita = new Visita();
        visita.setPadrinoId(1L);
        visita.setEncargadoId(1);
        visita.setFechaVisita(LocalDate.now().plusDays(1));
        visita.setHoraVisita(null);

        assertThrows(RuntimeException.class, () -> visitaService.saveVisita(visita));
        verify(visitaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Obtener todas las visitas")
    void getAllVisitas_ReturnsAllVisitas() {
        Visita visita1 = new Visita();
        Visita visita2 = new Visita();
        when(visitaRepository.findAll()).thenReturn(Arrays.asList(visita1, visita2));

        List<Visita> visitas = visitaService.getAllVisitas();

        assertEquals(2, visitas.size());
        verify(visitaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener visitas por padrino")
    void getVisitasByPadrino_ValidId_ReturnsVisitas() {
        Long padrinoId = 1L;
        Visita visita = new Visita();
        when(visitaRepository.findByPadrinoId(padrinoId)).thenReturn(Arrays.asList(visita));

        List<Visita> visitas = visitaService.getVisitasByPadrino(padrinoId);

        assertEquals(1, visitas.size());
        verify(visitaRepository, times(1)).findByPadrinoId(padrinoId);
    }

    @Test
    @DisplayName("Obtener visitas por encargado")
    void getVisitasByEncargado_ValidId_ReturnsVisitas() {
        Integer encargadoId = 1;
        Visita visita = new Visita();
        when(visitaRepository.findByEncargadoId(encargadoId)).thenReturn(Arrays.asList(visita));

        List<Visita> visitas = visitaService.getVisitasByEncargado(encargadoId);

        assertEquals(1, visitas.size());
        verify(visitaRepository, times(1)).findByEncargadoId(encargadoId);
    }

    @Test
    @DisplayName("Obtener visita por ID existente")
    void getVisitaById_ExistingId_ReturnsVisita() {
        Integer id = 1;
        Visita visita = new Visita();
        when(visitaRepository.findById(id)).thenReturn(Optional.of(visita));

        Visita foundVisita = visitaService.getVisitaById(id);

        assertNotNull(foundVisita);
        verify(visitaRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Obtener visita por ID inexistente - Debe lanzar excepción")
    void getVisitaById_NonExistingId_ThrowsException() {
        Integer id = 999;
        when(visitaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> visitaService.getVisitaById(id));
        verify(visitaRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Actualizar estado de visita")
    void updateEstadoVisita_ValidEstado_UpdatesVisita() {
        Integer id = 1;
        Visita.EstadoVisita nuevoEstado = Visita.EstadoVisita.ACEPTADA;
        Visita visita = new Visita();
        visita.setEstadoVisita(Visita.EstadoVisita.EN_REVISION);

        when(visitaRepository.findById(id)).thenReturn(Optional.of(visita));
        when(visitaRepository.save(any(Visita.class))).thenReturn(visita);

        Visita updatedVisita = visitaService.updateEstadoVisita(id, nuevoEstado);

        assertEquals(nuevoEstado, updatedVisita.getEstadoVisita());
        verify(visitaRepository, times(1)).findById(id);
        verify(visitaRepository, times(1)).save(visita);
    }
}