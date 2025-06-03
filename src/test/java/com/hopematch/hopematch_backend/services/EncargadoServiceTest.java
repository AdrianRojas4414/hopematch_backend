package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Encargado;
import com.hopematch.hopematch_backend.models.Nino;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EncargadoServiceTest {

    @Mock
    private EncargadoRepository encargadoRepository;

    @Mock
    private NinoRepository ninoRepository;

    @InjectMocks
    private EncargadoService encargadoService;

    private Encargado encargado;
    private Nino nino;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        encargado = new Encargado();
        encargado.setId(1);
        encargado.setNombre("Juan Perez");
        encargado.setEmail("juan@example.com");
        encargado.setCelular(123456789);
        encargado.setContrasenia("password123");
        encargado.setEstado("Activo");

        nino = new Nino();
        nino.setId(1);
        nino.setNombre("Pedro");
    }

    @Test
    @DisplayName("Guardar encargado con valores por defecto en fotos")
    void saveEncargado_WithDefaultPhotos() {
        encargado.setFoto(null);
        encargado.setFoto_hogar("");

        when(encargadoRepository.save(any(Encargado.class))).thenReturn(encargado);

        Encargado result = encargadoService.saveEncargado(encargado);

        assertNotNull(result);
        assertEquals("https://i.pinimg.com/736x/2c/f5/58/2cf558ab8c1f12b43f7326945672805e.jpg", result.getFoto());
        assertEquals("https://static.vecteezy.com/system/resources/previews/004/550/083/non_2x/houses-logo-illustration-free-vector.jpg", result.getFoto_hogar());
        assertTrue(result.getContrasenia().startsWith("$2a$")); // Verifica que se hasheó
    }

    @Test
    @DisplayName("Guardar encargado con fotos personalizadas")
    void saveEncargado_WithCustomPhotos() {
        encargado.setFoto("custom.jpg");
        encargado.setFoto_hogar("hogar.jpg");

        when(encargadoRepository.save(any(Encargado.class))).thenReturn(encargado);

        Encargado result = encargadoService.saveEncargado(encargado);

        assertNotNull(result);
        assertEquals("custom.jpg", result.getFoto());
        assertEquals("hogar.jpg", result.getFoto_hogar());
    }

    @Test
    @DisplayName("Obtener todos los encargados cuando hay datos")
    void getAllEncargados_WithData() {
        when(encargadoRepository.findAll()).thenReturn(List.of(encargado, new Encargado()));

        List<Encargado> result = encargadoService.getAllEncargados();

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Obtener todos los encargados cuando no hay datos")
    void getAllEncargados_Empty() {
        when(encargadoRepository.findAll()).thenReturn(List.of());

        List<Encargado> result = encargadoService.getAllEncargados();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Obtener encargado por ID existente")
    void getEncargadoById_Exists() {
        when(encargadoRepository.findById(1)).thenReturn(Optional.of(encargado));

        Optional<Encargado> result = encargadoService.getEncargadoById(1);

        assertTrue(result.isPresent());
        assertEquals("Juan Perez", result.get().getNombre());
    }

    @Test
    @DisplayName("Obtener encargado por ID no existente")
    void getEncargadoById_NotExists() {
        when(encargadoRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Encargado> result = encargadoService.getEncargadoById(99);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Buscar encargado por email existente")
    void findByEmail_Exists() {
        when(encargadoRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(encargado));

        Optional<Encargado> result = encargadoService.findByEmail("juan@example.com");

        assertTrue(result.isPresent());
        assertEquals("juan@example.com", result.get().getEmail());
    }

    @Test
    @DisplayName("Buscar encargado por email no existente")
    void findByEmail_NotExists() {
        when(encargadoRepository.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());

        Optional<Encargado> result = encargadoService.findByEmail("noexiste@example.com");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Agregar niño a encargado existente")
    void addNinoToEncargado_Success() {
        when(encargadoRepository.findById(1)).thenReturn(Optional.of(encargado));
        when(ninoRepository.save(any(Nino.class))).thenReturn(nino);

        Nino result = encargadoService.addNinoToEncargado(1, nino);

        assertNotNull(result);
        assertEquals("Pedro", result.getNombre());
        assertEquals(1, result.getEncargado().getId());
    }

    @Test
    @DisplayName("Agregar niño a encargado no existente")
    void addNinoToEncargado_EncargadoNotFound() {
        when(encargadoRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                encargadoService.addNinoToEncargado(99, nino)
        );
    }

    @Test
    @DisplayName("Actualizar encargado existente con todos los campos")
    void updateEncargado_AllFields() {
        Encargado updated = new Encargado();
        updated.setNombre("Juan Actualizado");
        updated.setEmail("juan.updated@example.com");
        updated.setCelular(987654321);
        updated.setContrasenia("newpassword");
        updated.setFoto("new.jpg");
        updated.setFoto_hogar("new_hogar.jpg");
        updated.setEstado("Suspendido");
        updated.setNombre_hogar("Hogar Actualizado");
        updated.setDireccion_hogar("Nueva dirección");
        updated.setDescripcion("Nueva descripción");

        when(encargadoRepository.findById(1)).thenReturn(Optional.of(encargado));
        when(encargadoRepository.save(any(Encargado.class))).thenReturn(updated);

        Encargado result = encargadoService.updateEncargado(1, updated);

        assertNotNull(result);
        assertEquals("Juan Actualizado", result.getNombre());
        assertEquals("juan.updated@example.com", result.getEmail());
        assertEquals(987654321, result.getCelular());
        assertEquals("new.jpg", result.getFoto());
        assertEquals("new_hogar.jpg", result.getFoto_hogar());
        assertEquals("Suspendido", result.getEstado());
    }

    @Test
    @DisplayName("Actualizar encargado sin cambiar contraseña")
    void updateEncargado_NoPasswordChange() {
        Encargado updated = new Encargado();
        updated.setNombre("Juan Actualizado");
        updated.setContrasenia(""); // Contraseña vacía no debería actualizarse

        when(encargadoRepository.findById(1)).thenReturn(Optional.of(encargado));
        when(encargadoRepository.save(any(Encargado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Encargado result = encargadoService.updateEncargado(1, updated);

        assertEquals("Juan Actualizado", result.getNombre());
        assertEquals("password123", encargado.getContrasenia()); // Mantiene la contraseña original
    }

    @Test
    @DisplayName("Actualizar encargado no existente")
    void updateEncargado_NotFound() {
        when(encargadoRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                encargadoService.updateEncargado(99, new Encargado())
        );
    }

    @Test
    @DisplayName("Obtener primer encargado cuando existe")
    void getFirstEncargado_Exists() {
        when(encargadoRepository.findAll()).thenReturn(List.of(encargado));

        Encargado result = encargadoService.getFirstEncargado();

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    @DisplayName("Obtener primer encargado cuando no existe")
    void getFirstEncargado_NotExists() {
        when(encargadoRepository.findAll()).thenReturn(List.of());

        assertThrows(RuntimeException.class, () ->
                encargadoService.getFirstEncargado()
        );
    }
}