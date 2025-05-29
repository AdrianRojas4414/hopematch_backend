package com.hopematch.hopematch_backend.controllers;


import com.hopematch.hopematch_backend.models.Encargado;
import com.hopematch.hopematch_backend.models.Nino;
import com.hopematch.hopematch_backend.services.EncargadoService;
import com.hopematch.hopematch_backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/encargado")
@CrossOrigin(origins = {"http://localhost:4200", "https://adrianrojas4414.github.io", "https://hopematch.publicvm.com", "https://hopematch.netlify.app"})
public class EncargadoController {

    @Autowired
    private EncargadoService encargadoService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/add")
    public Encargado createEncargado(@RequestBody Encargado encargado){
        return encargadoService.saveEncargado(encargado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Encargado> getEncargadoById(@PathVariable int id){
        Optional<Encargado> encargado = encargadoService.getEncargadoById(id);
        return new ResponseEntity<>(encargado.get(), HttpStatus.OK);
    }

    @GetMapping("/list")
    public List<Encargado> getAllEncargados(){
        return encargadoService.getAllEncargados();
    }

    @PostMapping("/{idEncargado}/add-nino")
    public Nino addNinoToEncargado(@PathVariable int idEncargado, @RequestBody Nino nino){
        return encargadoService.addNinoToEncargado(idEncargado, nino);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Encargado loginEncargado) {
        Optional<Encargado> encargadoOpt = encargadoService.findByEmail(loginEncargado.getEmail());
        if (encargadoOpt.isEmpty() || !loginEncargado.getContrasenia().equals(encargadoOpt.get().getContrasenia())) {
            return ResponseEntity.status(401)
                    .body("Usuario o contraseña incorrectos");
        }

        Encargado encargado = encargadoOpt.get();
        String token = jwtUtil.generateToken(encargado.getEmail(), "encargado", encargado.getId());
        String estado = encargado.getEstado().trim();

        String responseJson = String.format(
                "{\"token\": \"%s\", \"idEncargado\": %d, \"email\": \"%s\", \"estado\": \"%s\"",
                token, encargado.getId(), encargado.getEmail(), estado
        );

        if ("Rechazado".equalsIgnoreCase(estado)) {
            return ResponseEntity.status(401)
                    .body(responseJson += ", \"message\": \"Su cuenta está rechazada. Por favor, contáctese con un administrador.");
        } else if ("En revision".equalsIgnoreCase(estado)) {
            return ResponseEntity.status(401)
                    .body(responseJson += ", \"message\": \"Su cuenta está en revisión. Por favor, contáctese con un administrador.");
        }

        responseJson += "}";

        return ResponseEntity.ok(responseJson);
    }

    @PutMapping("/update/{id}")
    public Encargado updateEncargado(@PathVariable int id, @RequestBody Encargado encargadoDetails) {
        return encargadoService.updateEncargado(id, encargadoDetails);
    }
}
