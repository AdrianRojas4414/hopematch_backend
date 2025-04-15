package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Donacion;
import com.hopematch.hopematch_backend.models.Nino;
import com.hopematch.hopematch_backend.repositories.DonacionRepository;
import com.hopematch.hopematch_backend.repositories.EncargadoRepository;
import com.hopematch.hopematch_backend.repositories.PadrinoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DonacionServiceTest {
    @Mock
    private DonacionRepository donacionRepository;

    @InjectMocks
    private DonacionService donacionService;
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
    @DisplayName("El encargado agrega un comentario de agradecimiento por la donación")
    void  agregarComentarioEncargadoTest(){
        Integer donacionId = 1;
        String comentario = "Gracias por la donación";
        Donacion donacion = new Donacion();
        donacion.setId(donacionId);
        donacion.setComentarioEncargado(comentario);

        when(donacionRepository.findById(donacionId)).thenReturn(Optional.of(donacion));
        when(donacionRepository.save(any(Donacion.class))).thenReturn(donacion);

        Donacion donacionResult = donacionService.agregarComentarioEncargado(donacionId, comentario);

        assertNotNull(donacionResult);
        assertEquals(comentario, donacionResult.getComentarioEncargado());
    }

    @Test
    @DisplayName("El encargado agrega foto de la donación")
    void agregarFotoEncargadoTest(){
        Integer donacionId = 1;
        String fotoDonacion = "https://i.pinimg.com/originals/ef/c9/1a/efc91ab5bf77da1ed8fee4e4d337c9c7.jpg";
        Donacion donacion = new Donacion();
        donacion.setId(donacionId);
        donacion.setComentarioEncargado(fotoDonacion);

        when(donacionRepository.findById(donacionId)).thenReturn(Optional.of(donacion));
        when(donacionRepository.save(any(Donacion.class))).thenReturn(donacion);

        Donacion donacionResult = donacionService.agregarFotoEncargado(donacionId, fotoDonacion);

        assertNotNull(donacionResult);
        assertEquals(fotoDonacion, donacionResult.getFotoDonacion());
    }

    @Test
    void getDonacionesByPadrinoTest() {
        long padrinoId = 1L;
        Donacion donacion = new Donacion();
        when(donacionRepository.findByPadrinoId(padrinoId)).thenReturn(List.of(donacion));

        List<Donacion> resultado = donacionService.getDonacionesByPadrino(padrinoId);

        assertFalse(resultado.isEmpty());
        verify(donacionRepository).findByPadrinoId(padrinoId);
        verifyNoInteractions(padrinoRepository); // No se usa existsById
    }

    @Test
    void getDonacionesByEncargadoTest() {
        int encargadoId = 1;
        Donacion donacion = new Donacion();
        when(donacionRepository.findByEncargadoId(encargadoId)).thenReturn(List.of(donacion));

        List<Donacion> resultado = donacionService.getDonacionesByEncargado(encargadoId);

        assertFalse(resultado.isEmpty());
        verify(donacionRepository).findByEncargadoId(encargadoId);
        verifyNoInteractions(encargadoRepository); // No se usa existsById
    }

    @Test
    void getDonacionesByPadrinoTest_SinResultados() {
        long padrinoId = 99L;
        when(donacionRepository.findByPadrinoId(padrinoId)).thenReturn(List.of());

        List<Donacion> resultado = donacionService.getDonacionesByPadrino(padrinoId);

        assertTrue(resultado.isEmpty());
        verify(donacionRepository).findByPadrinoId(padrinoId);
    }

    @Test
    void getDonacionesByEncargadoTest_SinResultados() {
        int encargadoId = 99;
        when(donacionRepository.findByEncargadoId(encargadoId)).thenReturn(List.of());

        List<Donacion> resultado = donacionService.getDonacionesByEncargado(encargadoId);

        assertTrue(resultado.isEmpty());
        verify(donacionRepository).findByEncargadoId(encargadoId);
    }
}
