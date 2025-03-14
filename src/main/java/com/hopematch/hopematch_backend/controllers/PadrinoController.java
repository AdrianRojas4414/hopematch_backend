package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Padrino;
import com.hopematch.hopematch_backend.services.PadrinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class PadrinoController {

    @Autowired
    private PadrinoService padrinoService;

    @PostMapping("/addPadrino")
    public Padrino postPadrino(@RequestBody Padrino padrino){
        return padrinoService.savePadrino(padrino);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Padrino loginPadrino) {
        Optional<Padrino> padrino = padrinoService.findByEmail(loginPadrino.getEmail());
        if (padrino.isPresent() && passwordEncoder.matches(loginUser.getPassword(), padrino.get().getPassword())) {
            // Aquí puedes devolver un token JWT si estás implementando autenticación con tokens
            return ResponseEntity.ok("Inicio de sesión exitoso");
        } else {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
        }
    }

    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld(){
        return ResponseEntity.ok("Hello World! Esto es HOPEMATCH");
    }
}
