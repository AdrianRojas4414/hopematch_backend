package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Padrino;
import com.hopematch.hopematch_backend.repositories.PadrinoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PadrinoServiceTest {

    @Mock
    private PadrinoRepository padrinoRepository;

    @InjectMocks
    private PadrinoService padrinoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verifica que el Padrino y la foto por defecto se guarda correctamente.")
    void savePadrinoTest() {
        Padrino padrino = new Padrino();
        padrino.setNombre("Luis");
        padrino.setEmail("luis@example.com");
        padrino.setCelular(987654321);
        padrino.setContrasenia("password123");
        padrino.setFoto("");

        when(padrinoRepository.save(any(Padrino.class))).thenReturn(padrino);

        Padrino savedPadrino = padrinoService.savePadrino(padrino);
        assertNotNull(savedPadrino);
        assertEquals("Luis", savedPadrino.getNombre());
        assertEquals("https://i.pinimg.com/736x/2c/f5/58/2cf558ab8c1f12b43f7326945672805e.jpg", savedPadrino.getFoto());
    }

    @Test
    @DisplayName("Verifica que se obtienen todos los padrinos correctamente.")
    void getAllPadrinosTest() {
        when(padrinoRepository.findAll()).thenReturn(List.of(new Padrino(), new Padrino()));

        List<Padrino> padrinos = padrinoService.getAllPadrinos();
        assertNotNull(padrinos);
        assertEquals(2, padrinos.size());
    }

    @Test
    @DisplayName("Verifica que se encuentre a un Padrino por su Id")
    void getPadrinoByIdTest() {
        Padrino padrino = new Padrino();
        padrino.setNombre("Maria");

        when(padrinoRepository.findById(1)).thenReturn(Optional.of(padrino));

        Optional<Padrino> result = padrinoService.getPadrinoById(1);
        assertTrue(result.isPresent());
        assertEquals("Maria", result.get().getNombre());
    }

    @Test
    @DisplayName("Verifica que se encuentre a un Padrino por su email")
    void findByEmailTest() {
        Padrino padrino = new Padrino();
        padrino.setEmail("maria@example.com");

        when(padrinoRepository.findByEmail("maria@example.com")).thenReturn(Optional.of(padrino));

        Optional<Padrino> result = padrinoService.findByEmail("maria@example.com");
        assertTrue(result.isPresent());
        assertEquals("maria@example.com", result.get().getEmail());
    }

    @Test
    @DisplayName("Verifica que se pueda actualizar un Padrino")
    void updatePadrinoTest() {
        Padrino padrino = new Padrino();
        padrino.setNombre("Pedro");
        padrino.setEmail("pedro@example.com");

        Padrino updatedDetails = new Padrino();
        updatedDetails.setNombre("Pedro Actualizado");
        updatedDetails.setEmail("pedro_updated@example.com");

        when(padrinoRepository.findById(1)).thenReturn(Optional.of(padrino));
        when(padrinoRepository.save(any(Padrino.class))).thenReturn(updatedDetails);

        Padrino padrino_result = padrinoService.updatePadrino(1, updatedDetails);
        assertNotNull(padrino_result);
        assertEquals("Pedro Actualizado", padrino_result.getNombre());
        assertEquals("pedro_updated@example.com", padrino_result.getEmail());
    }
}