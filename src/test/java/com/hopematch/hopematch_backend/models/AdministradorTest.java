package com.hopematch.hopematch_backend.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AdministradorTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrect_ThenValidationSucceeds() {
        Administrador admin = new Administrador();
        admin.setNombre("Valid Name");
        admin.setEmail("valid@email.com");
        admin.setContrasenia("validPassword123");

        Set<ConstraintViolation<Administrador>> violations = validator.validate(admin);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenEmailInvalid_ThenValidationFails() {
        Administrador admin = new Administrador();
        admin.setNombre("Valid Name");
        admin.setEmail("invalid-email");
        admin.setContrasenia("validPassword123");

        Set<ConstraintViolation<Administrador>> violations = validator.validate(admin);
        assertEquals(1, violations.size());
        assertEquals("debe ser una dirección de correo electrónico con formato correcto",
                violations.iterator().next().getMessage());
    }

    @Test
    void whenFieldsAreNull_ThenValidationFails() {
        Administrador admin = new Administrador();

        Set<ConstraintViolation<Administrador>> violations = validator.validate(admin);
        assertEquals(3, violations.size());
    }

    @Test
    void getId_ShouldReturnCorrectId() {
        Administrador admin = new Administrador();
        admin.setId(5);

        assertEquals(5, admin.getId());
    }
}