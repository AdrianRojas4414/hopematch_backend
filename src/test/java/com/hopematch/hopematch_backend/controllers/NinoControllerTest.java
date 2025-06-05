package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Nino;
import com.hopematch.hopematch_backend.services.NinoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NinoControllerTest {

    @InjectMocks
    private NinoController ninoController;

    @Mock
    private NinoService ninoService;

    private MockMvc mockMvc;

    private Nino sampleNino;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ninoController).build();

        sampleNino = new Nino();
        sampleNino.setId(1);
        sampleNino.setCi(12345);
        sampleNino.setNombre("Juanito");
        sampleNino.setFechaNacimiento(new Date());
        sampleNino.setNecesidades(Arrays.asList("Comida", "Educación"));
    }

    @Test
    void createNino_ReturnsSavedNino() {
        when(ninoService.saveNino(any(Nino.class))).thenReturn(sampleNino);

        Nino created = ninoController.createNino(sampleNino);

        assertNotNull(created);
        assertEquals(sampleNino.getCi(), created.getCi());
        verify(ninoService).saveNino(sampleNino);
    }

    @Test
    void getNinoById_ReturnsNinoWrappedInResponseEntity() {
        when(ninoService.getNinoById(1)).thenReturn(Optional.of(sampleNino));

        ResponseEntity<Nino> response = ninoController.getNinoById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleNino, response.getBody());
    }

    @Test
    void getNinoByCi_ReturnsNinoWrappedInResponseEntity() {
        when(ninoService.getNinoByCi(12345)).thenReturn(Optional.of(sampleNino));

        ResponseEntity<Nino> response = ninoController.getNinoByCi(12345);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleNino, response.getBody());
    }

    @Test
    void getAllNinos_ReturnsList() {
        List<Nino> ninoList = Arrays.asList(sampleNino);
        when(ninoService.getAllNinos()).thenReturn(ninoList);

        List<Nino> result = ninoController.getAllNinos();

        assertEquals(1, result.size());
        verify(ninoService).getAllNinos();
    }

    @Test
    void updateNino_ReturnsUpdatedNino() {
        when(ninoService.updateNino(eq(1), any(Nino.class))).thenReturn(sampleNino);

        Nino result = ninoController.updateNino(1, sampleNino);

        assertEquals(sampleNino.getId(), result.getId());
        verify(ninoService).updateNino(1, sampleNino);
    }

    @Test
    void getNinosByEncargado_ReturnsListWrapped() {
        List<Nino> ninoList = Arrays.asList(sampleNino);
        when(ninoService.getNinosbyEncargado(2)).thenReturn(ninoList);

        ResponseEntity<List<Nino>> response = ninoController.getNinosByEncargado(2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getNecesidadesByEncargado_ReturnsNecesidades() {
        List<String> necesidades = Arrays.asList("Comida", "Educación");
        when(ninoService.getNecesidadesByEncargado(2)).thenReturn(necesidades);

        ResponseEntity<List<String>> response = ninoController.getNecesidadesByEncargado(2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void deleteNino_ReturnsOkMessage_WhenSuccessful() {
        doNothing().when(ninoService).deleteNino(1);

        ResponseEntity<String> response = ninoController.deleteNino(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("eliminado correctamente"));
    }

    @Test
    void deleteNino_ReturnsErrorMessage_WhenException() {
        doThrow(new RuntimeException("Error deleting")).when(ninoService).deleteNino(1);

        ResponseEntity<String> response = ninoController.deleteNino(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Error al eliminar"));
    }
}