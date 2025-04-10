package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Donacion;
import com.hopematch.hopematch_backend.models.Encargado;
import com.hopematch.hopematch_backend.models.Padrino;
import com.hopematch.hopematch_backend.repositories.DonacionRepository;
import com.hopematch.hopematch_backend.repositories.EncargadoRepository;
import com.hopematch.hopematch_backend.repositories.PadrinoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DonacionServiceTest {

    @Mock
    private DonacionRepository donacionRepository;

    @Mock
    private PadrinoRepository padrinoRepository;

    @Mock
    private EncargadoRepository encargadoRepository;

    @InjectMocks
    private DonacionService donacionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveDonacionTest_Success() {
        Donacion donacion = new Donacion();
        donacion.setCantidadDonacion(100.0);
        donacion.setFechaDonacion(LocalDate.now());

        Padrino padrino = new Padrino();
        padrino.setId(1);
        donacion.setPadrino(padrino);

        Encargado encargado = new Encargado();
        encargado.setId(1);
        donacion.setEncargado(encargado);

        when(padrinoRepository.findById(1)).thenReturn(Optional.of(padrino));
        when(encargadoRepository.findById(1)).thenReturn(Optional.of(encargado));
        when(donacionRepository.save(any(Donacion.class))).thenReturn(donacion);

        Donacion savedDonacion = donacionService.saveDonacion(donacion);

        assertNotNull(savedDonacion);
        assertEquals(100.0, savedDonacion.getCantidadDonacion());
        verify(donacionRepository, times(1)).save(donacion);
    }


    @Test
    void getDonacionByIdTest_Success() {
        Donacion donacion = new Donacion();
        donacion.setId(1);
        donacion.setCantidadDonacion(50.0);

        when(donacionRepository.findById(1)).thenReturn(Optional.of(donacion));

        Donacion foundDonacion = donacionService.getDonacionById(1);

        assertNotNull(foundDonacion);
        assertEquals(1, foundDonacion.getId());
        assertEquals(50.0, foundDonacion.getCantidadDonacion());
    }

    @Test
    void getDonacionByIdTest_NotFound() {
        when(donacionRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            donacionService.getDonacionById(1);
        });
        assertEquals("Donación no encontrada con ID: 1", exception.getMessage());
    }
}
