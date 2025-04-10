package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Donacion;
import com.hopematch.hopematch_backend.models.Padrino;
import com.hopematch.hopematch_backend.repositories.DonacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


class DonacionServiceTest {

    @Mock
    private DonacionRepository donacionRepository;

    @InjectMocks
    private DonacionService donacionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void saveDonacion() {
    }

    @Test
    void getAllDonacionesTest() {
        when(donacionRepository.findAll()).thenReturn(List.of(new Donacion(), new Donacion()));

        List<Donacion> donaciones = donacionService.getAllDonaciones();
        assertNotNull(donaciones);
        assertEquals(2, donaciones.size());
    }

    @Test
    void getAllDonacionesEmptyListTest() {
        when(donacionRepository.findAll()).thenReturn(List.of());

        List<Donacion> donaciones = donacionService.getAllDonaciones();

        assertNotNull(donaciones);
        assertTrue(donaciones.isEmpty());
    }

    @Test
    void getDonacionesByPadrino() {
    }

    @Test
    void getDonacionesByEncargado() {
    }

    @Test
    void getDonacionById() {
    }

    @Test
    void agregarComentarioEncargado() {
    }

    @Test
    void agregarFotoEncargado() {
    }
}