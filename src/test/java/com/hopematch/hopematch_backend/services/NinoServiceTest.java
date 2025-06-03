package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Encargado;
import com.hopematch.hopematch_backend.models.Nino;
import com.hopematch.hopematch_backend.repositories.EncargadoRepository;
import com.hopematch.hopematch_backend.repositories.NinoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NinoServiceTest {

    @Mock
    private NinoRepository ninoRepository;

    @Mock
    private EncargadoRepository encargadoRepository;

    @InjectMocks
    private NinoService ninoService;

    private Nino nino;
    private Encargado encargado;

    @BeforeEach
    void setUp() {
        encargado = new Encargado();
        encargado.setId(1);
        encargado.setNinos(new ArrayList<>());

        nino = new Nino();
        nino.setId(1);
        nino.setCi(123456);
        nino.setNombre("Juan");
        nino.setFechaNacimiento(new Date());
        nino.setNecesidades(Arrays.asList("Comida", "Educación"));
        nino.setEncargado(encargado);

        encargado.getNinos().add(nino);
    }

    @Test
    @DisplayName("Guardar un nuevo niño correctamente")
    void testSaveNino() {
        when(ninoRepository.save(any(Nino.class))).thenReturn(nino);

        Nino savedNino = ninoService.saveNino(nino);

        assertNotNull(savedNino);
        assertEquals("Juan", savedNino.getNombre());
        verify(ninoRepository, times(1)).save(nino);
    }

    @Test
    @DisplayName("Obtener un niño por ID existente")
    void testGetNinoByIdExists() {
        when(ninoRepository.findById(1)).thenReturn(Optional.of(nino));

        Optional<Nino> foundNino = ninoService.getNinoById(1);

        assertTrue(foundNino.isPresent());
        assertEquals("Juan", foundNino.get().getNombre());
    }

    @Test
    @DisplayName("Obtener un niño por CI existente")
    void testGetNinoByCiExists() {
        when(ninoRepository.findByCi(123456)).thenReturn(Optional.of(nino));

        Optional<Nino> foundNino = ninoService.getNinoByCi(123456);

        assertTrue(foundNino.isPresent());
        assertEquals(1, foundNino.get().getId());
    }

    @Test
    @DisplayName("Actualizar un niño existente correctamente")
    void testUpdateNinoSuccess() {
        Nino ninoDetails = new Nino();
        ninoDetails.setCi(654321);
        ninoDetails.setNombre("Carlos");
        ninoDetails.setFechaNacimiento(new Date());
        ninoDetails.setNecesidades(Collections.singletonList("Salud"));

        when(ninoRepository.findById(1)).thenReturn(Optional.of(nino));
        when(ninoRepository.save(any(Nino.class))).thenReturn(ninoDetails);

        Nino updatedNino = ninoService.updateNino(1, ninoDetails);

        assertEquals("Carlos", updatedNino.getNombre());
        assertEquals(654321, updatedNino.getCi());
        verify(ninoRepository, times(1)).save(nino);
    }

    @Test
    @DisplayName("Actualizar un niño no existente lanza excepción")
    void testUpdateNinoNotFound() {
        when(ninoRepository.findById(2)).thenReturn(Optional.empty());

        Nino ninoDetails = new Nino();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ninoService.updateNino(2, ninoDetails);
        });

        assertEquals("Niño no encontrado con id: 2", exception.getMessage());
    }

    @Test
    @DisplayName("Eliminar un niño existente correctamente")
    void testDeleteNinoSuccess() {
        when(ninoRepository.findById(1)).thenReturn(Optional.of(nino));
        when(encargadoRepository.save(any(Encargado.class))).thenReturn(encargado);

        assertDoesNotThrow(() -> ninoService.deleteNino(1));

        verify(ninoRepository, times(1)).delete(nino);
        verify(encargadoRepository, times(1)).save(encargado);
    }

    @Test
    @DisplayName("Eliminar un niño no existente lanza excepción")
    void testDeleteNinoNotFound() {
        when(ninoRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ninoService.deleteNino(99);
        });

        assertEquals("Niño no encontrado con id: 99", exception.getMessage());
    }
}