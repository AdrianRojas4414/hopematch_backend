package com.hopematch.hopematch_backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopematch.hopematch_backend.models.Donacion;
import com.hopematch.hopematch_backend.models.Encargado;
import com.hopematch.hopematch_backend.models.Padrino;
import com.hopematch.hopematch_backend.services.DonacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DonacionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DonacionService donacionService;

    @InjectMocks
    private DonacionController donacionController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(donacionController).build();
    }

    @Test
    @DisplayName("Agregar donación exitosamente")
    void agregarDonacionTest() throws Exception {
        Donacion donacion = new Donacion();
        donacion.setCantidadDonacion(100.0);
        donacion.setFechaDonacion(LocalDate.now());
        donacion.setPadrino(new Padrino());
        donacion.setEncargado(new Encargado());

        when(donacionService.saveDonacion(any(Donacion.class))).thenReturn(donacion);

        mockMvc.perform(post("/donaciones/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(donacion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidadDonacion", is(100.0)));
    }

    @Test
    @DisplayName("Listar todas las donaciones")
    void listarDonacionesTest() throws Exception {
        Donacion d1 = new Donacion();
        Donacion d2 = new Donacion();

        when(donacionService.getAllDonaciones()).thenReturn(List.of(d1, d2));

        mockMvc.perform(get("/donaciones/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Listar donaciones por padrino")
    void listarDonacionesPorPadrinoTest() throws Exception {
        when(donacionService.getDonacionesByPadrino(1L)).thenReturn(List.of(new Donacion()));

        mockMvc.perform(get("/donaciones/by-padrino/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("Obtener donación por ID")
    void obtenerDonacionPorIdTest() throws Exception {
        Donacion donacion = new Donacion();
        donacion.setId(1);

        when(donacionService.getDonacionById(1)).thenReturn(donacion);

        mockMvc.perform(get("/donaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @DisplayName("Agregar comentario del encargado")
    void agregarComentarioTest() throws Exception {
        Donacion donacion = new Donacion();
        donacion.setComentarioEncargado("Comentario de prueba");

        when(donacionService.agregarComentarioEncargado(eq(1), anyString())).thenReturn(donacion);

        mockMvc.perform(post("/donaciones/1/comentario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"Comentario de prueba\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comentarioEncargado", is("Comentario de prueba")));
    }

    @Test
    @DisplayName("Agregar foto de donación")
    void agregarFotoTest() throws Exception {
        Donacion donacion = new Donacion();
        donacion.setFotoDonacion("foto.jpg");

        when(donacionService.agregarFotoEncargado(eq(1), anyString())).thenReturn(donacion);

        mockMvc.perform(put("/donaciones/1/foto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("fotoUrl", "foto.jpg"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fotoDonacion", is("foto.jpg")));
    }

    @Test
    @DisplayName("Agregar fotos de progreso")
    void agregarFotosProgresoTest() throws Exception {
        Donacion donacion = new Donacion();
        donacion.setFotosProgreso(List.of("f1.jpg", "f2.jpg"));

        when(donacionService.agregarFotosProgreso(eq(1), anyList())).thenReturn(donacion);

        mockMvc.perform(post("/donaciones/1/fotos-progreso")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("fotos", List.of("f1.jpg", "f2.jpg")))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fotosProgreso", hasSize(2)));
    }

    @Test
    @DisplayName("Eliminar foto de progreso")
    void eliminarFotoProgresoTest() throws Exception {
        Donacion donacion = new Donacion();
        donacion.setFotosProgreso(List.of("f1.jpg"));

        when(donacionService.eliminarFotoProgreso(eq(1), eq(0))).thenReturn(donacion);

        mockMvc.perform(delete("/donaciones/1/fotos-progreso/0"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Actualizar fotos de progreso")
    void actualizarFotosProgresoTest() throws Exception {
        Donacion donacion = new Donacion();
        donacion.setFotosProgreso(List.of("f1.jpg", "f2.jpg"));

        when(donacionService.actualizarFotosProgreso(eq(1), anyList())).thenReturn(donacion);

        mockMvc.perform(put("/donaciones/1/fotos-progreso")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("fotos", List.of("f1.jpg", "f2.jpg")))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fotosProgreso", hasSize(2)));
    }
}
