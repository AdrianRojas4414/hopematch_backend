package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Administrador;
import com.hopematch.hopematch_backend.services.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@CrossOrigin("http://localhost:4200/")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @PostMapping("/add")
    public Administrador postAdministrador(@RequestBody Administrador administrador) {
        return administradorService.saveAdministrador(administrador);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Administrador loginAdmin) {
        Optional<Administrador> admin = administradorService.findByEmail(loginAdmin.getEmail());
        if (admin.isPresent() && loginAdmin.getContrasenia().equals(admin.get().getContrasenia())) {
            return ResponseEntity.ok("{\"id\": \"" + admin.get().getId() + "\", \"userType\": \"Admin\"}");
        } else {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
        }
    }

    @GetMapping("/list")
    public List<Administrador> getAllAdministradores() {
        return administradorService.getAllAdministradores();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administrador> getAdministradorById(@PathVariable int id) {
        Optional<Administrador> admin = administradorService.getAdministradorById(id);
        return admin.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public Administrador updateAdministrador(@PathVariable int id, @RequestBody Administrador adminDetails) {
        return administradorService.updateAdministrador(id, adminDetails);
    }
}