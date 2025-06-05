package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Administrador;
import com.hopematch.hopematch_backend.repositories.AdministradorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdministradorServiceTest {

    @Mock
    private AdministradorRepository administradorRepository;

    @InjectMocks
    private AdministradorService administradorService;

    private Administrador admin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        admin = new Administrador();
        admin.setId(1);
        admin.setNombre("Admin Test");
        admin.setEmail("admin@test.com");
        admin.setContrasenia("password123");
    }

    @Test
    @DisplayName("Guardar administrador - Éxito")
    void saveAdministrador_Success() {
        when(administradorRepository.save(any(Administrador.class))).thenReturn(admin);

        Administrador savedAdmin = administradorService.saveAdministrador(admin);

        assertNotNull(savedAdmin);
        assertEquals("Admin Test", savedAdmin.getNombre());
        assertTrue(savedAdmin.getContrasenia().startsWith("$2a$")); // Verificar que la contraseña está hasheada
        verify(administradorRepository, times(1)).save(any(Administrador.class));
    }

    @Test
    @DisplayName("Obtener todos los administradores")
    void getAllAdministradores_ReturnsAll() {
        when(administradorRepository.findAll()).thenReturn(Arrays.asList(admin, new Administrador()));

        List<Administrador> administradores = administradorService.getAllAdministradores();

        assertEquals(2, administradores.size());
        verify(administradorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener administrador por ID existente")
    void getAdministradorById_ExistingId_ReturnsAdmin() {
        when(administradorRepository.findById(1)).thenReturn(Optional.of(admin));

        Optional<Administrador> foundAdmin = administradorService.getAdministradorById(1);

        assertTrue(foundAdmin.isPresent());
        assertEquals("admin@test.com", foundAdmin.get().getEmail());
        verify(administradorRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Obtener administrador por ID inexistente")
    void getAdministradorById_NonExistingId_ReturnsEmpty() {
        when(administradorRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Administrador> foundAdmin = administradorService.getAdministradorById(999);

        assertTrue(foundAdmin.isEmpty());
        verify(administradorRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Buscar administrador por email existente")
    void findByEmail_ExistingEmail_ReturnsAdmin() {
        when(administradorRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));

        Optional<Administrador> foundAdmin = administradorService.findByEmail("admin@test.com");

        assertTrue(foundAdmin.isPresent());
        assertEquals(1, foundAdmin.get().getId());
        verify(administradorRepository, times(1)).findByEmail("admin@test.com");
    }

    @Test
    @DisplayName("Buscar administrador por email inexistente")
    void findByEmail_NonExistingEmail_ReturnsEmpty() {
        when(administradorRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        Optional<Administrador> foundAdmin = administradorService.findByEmail("nonexistent@test.com");

        assertTrue(foundAdmin.isEmpty());
        verify(administradorRepository, times(1)).findByEmail("nonexistent@test.com");
    }

    @Test
    @DisplayName("Actualizar administrador - Éxito")
    void updateAdministrador_Success() {
        Administrador existingAdmin = new Administrador();
        existingAdmin.setId(1);
        existingAdmin.setNombre("Admin Original");
        existingAdmin.setEmail("original@test.com");
        existingAdmin.setContrasenia(BCrypt.hashpw("oldpassword", BCrypt.gensalt()));

        Administrador updatedDetails = new Administrador();
        updatedDetails.setNombre("Admin Updated");
        updatedDetails.setEmail("updated@test.com");
        updatedDetails.setContrasenia("newpassword123");

        when(administradorRepository.findById(1)).thenReturn(Optional.of(existingAdmin));
        when(administradorRepository.save(any(Administrador.class))).thenAnswer(invocation -> {
            Administrador savedAdmin = invocation.getArgument(0);
            assertTrue(savedAdmin.getContrasenia().startsWith("$2a$"));
            return savedAdmin;
        });

        Administrador result = administradorService.updateAdministrador(1, updatedDetails);

        assertNotNull(result);
        assertEquals("Admin Updated", result.getNombre());
        assertEquals("updated@test.com", result.getEmail());
        assertTrue(result.getContrasenia().startsWith("$2a$")); // Verificar que la nueva contraseña está hasheada
        verify(administradorRepository, times(1)).findById(1);
        verify(administradorRepository, times(1)).save(any(Administrador.class));
    }

    @Test
    @DisplayName("Actualizar administrador sin cambiar contraseña")
    void updateAdministrador_NoPasswordChange() {
        Administrador updatedDetails = new Administrador();
        updatedDetails.setNombre("Admin Updated");
        updatedDetails.setEmail("updated@test.com");
        updatedDetails.setContrasenia(""); // Contraseña vacía no debería actualizarse

        when(administradorRepository.findById(1)).thenReturn(Optional.of(admin));
        when(administradorRepository.save(any(Administrador.class))).thenReturn(admin);

        Administrador result = administradorService.updateAdministrador(1, updatedDetails);

        assertNotNull(result);
        assertEquals("Admin Updated", result.getNombre());
        assertEquals("updated@test.com", result.getEmail());
        verify(administradorRepository, times(1)).findById(1);
        verify(administradorRepository, times(1)).save(any(Administrador.class));
    }

    @Test
    @DisplayName("Actualizar administrador inexistente - Lanza excepción")
    void updateAdministrador_NonExistingAdmin_ThrowsException() {
        Administrador updatedDetails = new Administrador();
        when(administradorRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> administradorService.updateAdministrador(999, updatedDetails));
        verify(administradorRepository, times(1)).findById(999);
        verify(administradorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Verificar contraseña correcta")
    void verifyPassword_CorrectPassword_ReturnsTrue() {
        String plainPassword = "password123";
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        boolean result = administradorService.verifyPassword(plainPassword, hashedPassword);

        assertTrue(result);
    }

    @Test
    @DisplayName("Verificar contraseña incorrecta")
    void verifyPassword_WrongPassword_ReturnsFalse() {
        String hashedPassword = BCrypt.hashpw("password123", BCrypt.gensalt());

        boolean result = administradorService.verifyPassword("wrongpassword", hashedPassword);

        assertFalse(result);
    }

    @Test
    @DisplayName("Guardar administrador con email duplicado - Debería permitirlo si el repositorio lo permite")
    void saveAdministrador_DuplicateEmail() {
        when(administradorRepository.save(any(Administrador.class))).thenReturn(admin);

        Administrador result = administradorService.saveAdministrador(admin);

        assertNotNull(result);
        verify(administradorRepository, times(1)).save(any(Administrador.class));
    }

    @Test
    @DisplayName("Actualizar administrador con contraseña nula")
    void updateAdministrador_NullPassword() {
        Administrador existingAdmin = new Administrador(1, "Admin", "admin@test.com", "oldHash");
        Administrador updatedDetails = new Administrador();
        updatedDetails.setNombre("Updated");
        updatedDetails.setEmail("updated@test.com");
        updatedDetails.setContrasenia(null); // Contraseña nula

        when(administradorRepository.findById(1)).thenReturn(Optional.of(existingAdmin));
        when(administradorRepository.save(any(Administrador.class))).thenReturn(existingAdmin);

        Administrador result = administradorService.updateAdministrador(1, updatedDetails);

        assertEquals("oldHash", result.getContrasenia());
    }

    @Test
    @DisplayName("Verificar contraseña con texto plano vacío - Debería devolver false")
    void verifyPassword_EmptyPlain_ReturnsFalse() {
        String hashed = BCrypt.hashpw("password", BCrypt.gensalt());
        assertFalse(administradorService.verifyPassword("", hashed));
    }

    @Test
    @DisplayName("Actualizar administrador con contraseña vacía - No debe actualizar hash")
    void updateAdministrador_EmptyPassword_KeepsOldHash() {
        Administrador existing = new Administrador(1, "Admin", "admin@test.com", "oldHash");
        Administrador update = new Administrador();
        update.setContrasenia("");

        when(administradorRepository.findById(1)).thenReturn(Optional.of(existing));
        when(administradorRepository.save(any())).thenReturn(existing);

        Administrador result = administradorService.updateAdministrador(1, update);

        assertEquals("oldHash", result.getContrasenia());
    }
}
