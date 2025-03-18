package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Nino;
import com.hopematch.hopematch_backend.repositories.NinoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NinoServiceTest {

    @Mock
    private NinoRepository ninoRepository;

    @InjectMocks
    private NinoService ninoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verifica que el Niño se guarda correctamente.")
    void saveNinoTest() {
        Nino nino = new Nino();
        nino.setNombre("Pedro");
        nino.setCi(123456);

        when(ninoRepository.save(any(Nino.class))).thenReturn(nino);

        Nino savedNino = ninoService.saveNino(nino);
        assertNotNull(savedNino);
        assertEquals("Pedro", savedNino.getNombre());
    }

    @Test
    @DisplayName("Verifica que se encuentra un Niño por su Id")
    void getNinoByIdTest() {
        Nino nino = new Nino();
        nino.setNombre("Carlos");

        when(ninoRepository.findById(1)).thenReturn(Optional.of(nino));

        Optional<Nino> result = ninoService.getNinoById(1);
        assertTrue(result.isPresent());
        assertEquals("Carlos", result.get().getNombre());
    }

    @Test
    @DisplayName("Verifica que se encuentra un Niño por su CI")
    void getNinoByCiTest() {
        Nino nino = new Nino();
        nino.setCi(987654);

        when(ninoRepository.findByCi(987654)).thenReturn(Optional.of(nino));

        Optional<Nino> result = ninoService.getNinoByCi(987654);
        assertTrue(result.isPresent());
        assertEquals(987654, result.get().getCi());
    }

    @Test
    @DisplayName("Verifica que se obtienen todos los Niños")
    void getAllNinosTest() {
        Nino nino1 = new Nino();
        nino1.setNombre("Pedro");
        Nino nino2 = new Nino();
        nino2.setNombre("Maria");

        List<Nino> ninos = Arrays.asList(nino1, nino2);
        when(ninoRepository.findAll()).thenReturn(ninos);

        List<Nino> ninos_result = ninoService.getAllNinos();
        assertEquals(2, ninos_result.size());
    }

    @Test
    @DisplayName("Verifica que se actualiza un Niño correctamente")
    void updateNinoTest() {
        Nino existingNino = new Nino();
        existingNino.setNombre("Pedro");

        Nino updatedNino = new Nino();
        updatedNino.setNombre("Juan");
        updatedNino.setCi(112233);

        when(ninoRepository.findById(1)).thenReturn(Optional.of(existingNino));
        when(ninoRepository.save(any(Nino.class))).thenReturn(updatedNino);

        Nino nino_result = ninoService.updateNino(1, updatedNino);
        assertEquals("Juan", nino_result.getNombre());
        assertEquals(112233, nino_result.getCi());
    }


    @Test
    @DisplayName("Verifica que se lanza una excepción si el Niño no es encontrado al actualizar")
    void updateNinoNotFoundTest() {
        Nino updatedNino = new Nino();
        updatedNino.setNombre("Luis");

        when(ninoRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            ninoService.updateNino(99, updatedNino);
        });
        assertEquals("Niño no encontrado con id: 99", exception.getMessage());
    }
}