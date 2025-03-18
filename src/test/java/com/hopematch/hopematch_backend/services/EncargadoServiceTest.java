package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Encargado;
import com.hopematch.hopematch_backend.models.Nino;
import com.hopematch.hopematch_backend.models.Padrino;
import com.hopematch.hopematch_backend.repositories.EncargadoRepository;
import com.hopematch.hopematch_backend.repositories.NinoRepository;
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

class EncargadoServiceTest {

    @Mock
    private EncargadoRepository encargadoRepository;

    @InjectMocks
    private EncargadoService encargadoService;

    @Mock
    private NinoRepository ninoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Verifica que el Encargado se guarda correctamente.")
    void saveEncargadoTest() {
        Encargado encargado = new Encargado();
        encargado.setNombre("Juan");
        encargado.setEmail("juan@example.com");
        encargado.setCelular(123456789);
        encargado.setContrasenia("password123");

        when(encargadoRepository.save(any(Encargado.class))).thenReturn(encargado);

        Encargado savedEncargado = encargadoService.saveEncargado(encargado);
        assertNotNull(savedEncargado);
        assertEquals("Juan", savedEncargado.getNombre());
    }

    @Test
    @DisplayName("Verifica que se obtienen todos los encargados correctamente.")
    void getAllEncargadosTest(){
        when(encargadoRepository.findAll()).thenReturn(List.of(new Encargado(), new Encargado()));

        List<Encargado> encargados = encargadoService.getAllEncargados();
        assertNotNull(encargados);
        assertEquals(2, encargados.size());
    }

    @Test
    @DisplayName("Verifica que se encuentre a un encargado por su Id")
    void getEncargadoByIdTest() {
        Encargado encargado = new Encargado();
        encargado.setNombre("Carlos");

        when(encargadoRepository.findById(1)).thenReturn(Optional.of(encargado));

        Optional<Encargado> result = encargadoService.getEncargadoById(1);
        assertTrue(result.isPresent());
        assertEquals("Carlos", result.get().getNombre());
    }

    @Test
    @DisplayName("Verifica que se encuentre a unn encargado por su email")
    void findByEmailTest() {
        Encargado encargado = new Encargado();
        encargado.setEmail("carlos@example.com");

        when(encargadoRepository.findByEmail("carlos@example.com")).thenReturn(Optional.of(encargado));

        Optional<Encargado> result = encargadoService.findByEmail("carlos@example.com");
        assertTrue(result.isPresent());
        assertEquals("carlos@example.com", result.get().getEmail());
    }

    @Test
    @DisplayName("Verifica que el encargado puede crear ninos")
    void addNinoToEncargadoTest() {
        Encargado encargado = new Encargado();
        encargado.setId(1);
        Nino nino = new Nino();
        nino.setNombre("Pedro");

        when(encargadoRepository.findById(1)).thenReturn(Optional.of(encargado));
        when(ninoRepository.save(any(Nino.class))).thenReturn(nino);

        Nino ninoResult = encargadoService.addNinoToEncargado(1, nino);
        assertNotNull(ninoResult);
        assertEquals("Pedro", ninoResult.getNombre());
    }

    @Test
    @DisplayName("Verifica que se pueda actualizar un Encargado")
    void updateEncargadoTest(){
        Encargado encargado = new Encargado();
        encargado.setNombre("Pedro");
        encargado.setEmail("pedro@example.com");

        Encargado updatedDetails = new Encargado();
        updatedDetails.setNombre("Pedro Actualizado");
        updatedDetails.setEmail("pedro_updated@example.com");

        when(encargadoRepository.findById(1)).thenReturn(Optional.of(encargado));
        when(encargadoRepository.save(any(Encargado.class))).thenReturn(updatedDetails);

        Encargado encargado_result = encargadoService.updateEncargado(1, updatedDetails);
        assertNotNull(encargado_result);
        assertEquals("Pedro Actualizado", encargado_result.getNombre());
        assertEquals("pedro_updated@example.com", encargado_result.getEmail());
    }
}