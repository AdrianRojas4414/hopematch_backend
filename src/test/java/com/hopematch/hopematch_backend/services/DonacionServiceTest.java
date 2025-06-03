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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    private Donacion donacion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        donacion = new Donacion();
        donacion.setId(1);
        donacion.setFotosProgreso(new ArrayList<>(List.of("a.jpg")));
        when(donacionRepository.findById(1)).thenReturn(Optional.of(donacion));
    }

    @Test
    @DisplayName("El encargado agrega un comentario de agradecimiento por la donación")
    void agregarComentarioEncargadoTest() {
        Integer donacionId = 1;
        String comentario = "Gracias por la donación";
        Donacion donacion = new Donacion();
        donacion.setId(donacionId);

        when(donacionRepository.findById(donacionId)).thenReturn(Optional.of(donacion));
        when(donacionRepository.save(any(Donacion.class))).thenReturn(donacion);

        Donacion result = donacionService.agregarComentarioEncargado(donacionId, comentario);

        assertNotNull(result);
        assertEquals(comentario, result.getComentarioEncargado());
    }

    @Test
    @DisplayName("El encargado agrega foto de la donación")
    void agregarFotoEncargadoTest() {
        Integer donacionId = 1;
        String foto = "foto.jpg";
        Donacion donacion = new Donacion();
        donacion.setId(donacionId);

        when(donacionRepository.findById(donacionId)).thenReturn(Optional.of(donacion));
        when(donacionRepository.save(any(Donacion.class))).thenReturn(donacion);

        Donacion result = donacionService.agregarFotoEncargado(donacionId, foto);

        assertNotNull(result);
        assertEquals(foto, result.getFotoDonacion());
    }

    @Test
    @DisplayName("Obtener todas las donaciones")
    void getAllDonacionesTest() {
        when(donacionRepository.findAll()).thenReturn(List.of(new Donacion(), new Donacion()));

        List<Donacion> result = donacionService.getAllDonaciones();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Guardar donación exitosamente")
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

        Donacion result = donacionService.saveDonacion(donacion);

        assertNotNull(result);
        assertEquals(100.0, result.getCantidadDonacion());
    }

    @Test
    @DisplayName("Guardar donación falla por cantidad inválida")
    void saveDonacionTest_FailCantidad() {
        Donacion donacion = new Donacion();
        donacion.setCantidadDonacion(0.0);

        Exception ex = assertThrows(RuntimeException.class, () -> donacionService.saveDonacion(donacion));
        assertEquals("La cantidad debe ser mayor a cero", ex.getMessage());
    }

    @Test
    @DisplayName("Guardar donación falla por padrino inválido")
    void saveDonacionTest_FailPadrino() {
        Donacion donacion = new Donacion();
        donacion.setCantidadDonacion(10.0);
        donacion.setPadrino(new Padrino()); // id = 0 por defecto

        Exception ex = assertThrows(RuntimeException.class, () -> donacionService.saveDonacion(donacion));
        assertEquals("Se requiere un padrino válido para la donación", ex.getMessage());
    }

    @Test
    @DisplayName("Guardar donación falla por padrino no encontrado")
    void saveDonacionTest_FailPadrinoNoEncontrado() {
        Donacion donacion = new Donacion();
        donacion.setCantidadDonacion(10.0);
        Padrino padrino = new Padrino();
        padrino.setId(99);
        donacion.setPadrino(padrino);

        when(padrinoRepository.findById(99)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> donacionService.saveDonacion(donacion));
        assertEquals("Padrino no encontrado con ID: 99", ex.getMessage());
    }

    @Test
    @DisplayName("Guardar donación falla por encargado inválido")
    void saveDonacionTest_FailEncargado() {
        Donacion donacion = new Donacion();
        donacion.setCantidadDonacion(10.0);
        donacion.setPadrino(new Padrino(){ { setId(1); } });
        donacion.setEncargado(new Encargado()); // id=0

        when(padrinoRepository.findById(1)).thenReturn(Optional.of(new Padrino(){ { setId(1); } }));

        Exception ex = assertThrows(RuntimeException.class, () -> donacionService.saveDonacion(donacion));
        assertEquals("Se requiere un encargado válido para la donación", ex.getMessage());
    }

    @Test
    @DisplayName("Guardar donación falla por encargado no encontrado")
    void saveDonacionTest_FailEncargadoNoEncontrado() {
        Donacion donacion = new Donacion();
        donacion.setCantidadDonacion(10.0);
        donacion.setPadrino(new Padrino(){ { setId(1); } });
        donacion.setEncargado(new Encargado(){ { setId(99); } });

        when(padrinoRepository.findById(1)).thenReturn(Optional.of(new Padrino(){ { setId(1); } }));
        when(encargadoRepository.findById(99)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> donacionService.saveDonacion(donacion));
        assertEquals("Encargado no encontrado con ID: 99", ex.getMessage());
    }

    @Test
    @DisplayName("Obtener donación por ID exitosamente")
    void getDonacionByIdTest_Success() {
        Donacion donacion = new Donacion();
        donacion.setId(1);

        when(donacionRepository.findById(1)).thenReturn(Optional.of(donacion));

        Donacion result = donacionService.getDonacionById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    @DisplayName("Obtener donación por ID falla cuando no existe")
    void getDonacionByIdTest_NotFound() {
        when(donacionRepository.findById(99)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> donacionService.getDonacionById(99));
        assertEquals("Donación no encontrada con ID: 99", ex.getMessage());
    }

    @Test
    @DisplayName("Obtener donaciones por padrino")
    void getDonacionesByPadrinoTest() {
        List<Donacion> lista = List.of(new Donacion(), new Donacion());
        when(donacionRepository.findByPadrinoId(1L)).thenReturn(lista);

        List<Donacion> result = donacionService.getDonacionesByPadrino(1L);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Obtener donaciones por encargado")
    void getDonacionesByEncargadoTest() {
        List<Donacion> lista = List.of(new Donacion(), new Donacion(), new Donacion());
        when(donacionRepository.findByEncargadoId(1)).thenReturn(lista);

        List<Donacion> result = donacionService.getDonacionesByEncargado(1);

        assertEquals(3, result.size());
    }
}