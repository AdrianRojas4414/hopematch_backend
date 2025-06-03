package com.hopematch.hopematch_backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopematch.hopematch_backend.models.Encargado;
import com.hopematch.hopematch_backend.models.Nino;
import com.hopematch.hopematch_backend.services.EncargadoService;
import com.hopematch.hopematch_backend.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class EncargadoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EncargadoService encargadoService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private EncargadoController encargadoController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(encargadoController).build();
    }

    private Encargado getSampleEncargado() {
        Encargado e = new Encargado();
        e.setId(1);
        e.setNombre("Juan");
        e.setCelular(12345678);
        e.setEmail("juan@example.com");
        e.setContrasenia("hashed");
        e.setEstado("Activo");
        e.setFoto("foto.jpg");
        e.setFoto_hogar("hogar.jpg");
        e.setNombre_hogar("Hogar Esperanza");
        e.setDireccion_hogar("Calle Falsa 123");
        e.setDescripcion("Descripción");
        return e;
    }

    @Test
    void testCreateEncargado() throws Exception {
        Encargado encargado = getSampleEncargado();
        Mockito.when(encargadoService.saveEncargado(any())).thenReturn(encargado);

        mockMvc.perform(post("/encargado/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(encargado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    void testGetEncargadoById() throws Exception {
        Mockito.when(encargadoService.getEncargadoById(1)).thenReturn(Optional.of(getSampleEncargado()));

        mockMvc.perform(get("/encargado/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetAllEncargados() throws Exception {
        Mockito.when(encargadoService.getAllEncargados()).thenReturn(Arrays.asList(getSampleEncargado()));

        mockMvc.perform(get("/encargado/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("juan@example.com"));
    }

    @Test
    void testAddNinoToEncargado() throws Exception {
        Nino nino = new Nino();
        nino.setId(1);
        nino.setNombre("Pepe");

        Mockito.when(encargadoService.addNinoToEncargado(eq(1), any(Nino.class))).thenReturn(nino);

        mockMvc.perform(post("/encargado/1/add-nino")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nino)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pepe"));
    }

    @Test
    void testLogin_estadoActivo() throws Exception {
        Encargado login = getSampleEncargado();
        login.setEstado("Activo");
        login.setContrasenia("plainpass");

        Mockito.when(encargadoService.findByEmail(anyString())).thenReturn(Optional.of(login));
        Mockito.when(encargadoService.verifyPassword(anyString(), anyString())).thenReturn(true);
        Mockito.when(jwtUtil.generateToken(anyString(), anyString(), anyInt())).thenReturn("jwt_token");

        mockMvc.perform(post("/encargado/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("token")));
    }

    @Test
    void testLogin_estadoEnRevision() throws Exception {
        Encargado login = getSampleEncargado();
        login.setEstado("En revision");

        Mockito.when(encargadoService.findByEmail(anyString())).thenReturn(Optional.of(login));
        Mockito.when(encargadoService.verifyPassword(anyString(), anyString())).thenReturn(true);
        Mockito.when(jwtUtil.generateToken(anyString(), anyString(), anyInt())).thenReturn("token_rev");

        mockMvc.perform(post("/encargado/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("en revisión")));
    }

    @Test
    void testLogin_estadoSuspendido() throws Exception {
        Encargado login = getSampleEncargado();
        login.setEstado("Suspendido");

        Mockito.when(encargadoService.findByEmail(anyString())).thenReturn(Optional.of(login));
        Mockito.when(encargadoService.verifyPassword(anyString(), anyString())).thenReturn(true);
        Mockito.when(jwtUtil.generateToken(anyString(), anyString(), anyInt())).thenReturn("token_susp");

        mockMvc.perform(post("/encargado/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("suspendida")));
    }

    @Test
    void testLogin_usuarioIncorrecto() throws Exception {
        Encargado login = getSampleEncargado();

        Mockito.when(encargadoService.findByEmail(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/encargado/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("incorrectos")));
    }

    @Test
    void testLogin_contraseniaIncorrecta() throws Exception {
        Encargado login = getSampleEncargado();

        Mockito.when(encargadoService.findByEmail(anyString())).thenReturn(Optional.of(login));
        Mockito.when(encargadoService.verifyPassword(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/encargado/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("incorrectos")));
    }

    @Test
    void testUpdateEncargado() throws Exception {
        Encargado updated = getSampleEncargado();
        updated.setNombre("Nuevo Nombre");

        Mockito.when(encargadoService.updateEncargado(eq(1), any(Encargado.class))).thenReturn(updated);

        mockMvc.perform(put("/encargado/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nuevo Nombre"));
    }
}