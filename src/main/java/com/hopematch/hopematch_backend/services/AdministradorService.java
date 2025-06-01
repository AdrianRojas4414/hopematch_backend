package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Administrador;
import com.hopematch.hopematch_backend.repositories.AdministradorRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    public Administrador saveAdministrador(Administrador administrador) {
        String hashedPassword = BCrypt.hashpw(administrador.getContrasenia(), BCrypt.gensalt());
        administrador.setContrasenia(hashedPassword);

        return administradorRepository.save(administrador);
    }

    public List<Administrador> getAllAdministradores() {
        return administradorRepository.findAll();
    }

    public Optional<Administrador> getAdministradorById(int id) {
        return administradorRepository.findById(id);
    }

    public Optional<Administrador> findByEmail(String email) {
        return administradorRepository.findByEmail(email);
    }

    public Administrador updateAdministrador(int id, Administrador administradorDetails) {
        return administradorRepository.findById(id).map(administrador -> {
            administrador.setNombre(administradorDetails.getNombre());
            administrador.setEmail(administradorDetails.getEmail());

            if (administradorDetails.getContrasenia() != null && !administradorDetails.getContrasenia().isEmpty()) {
                String hashedPassword = BCrypt.hashpw(administradorDetails.getContrasenia(), BCrypt.gensalt());
                administrador.setContrasenia(hashedPassword);
            }

            return administradorRepository.save(administrador);
        }).orElseThrow(() -> new RuntimeException("Administrador no encontrado con id: " + id));
    }

    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}