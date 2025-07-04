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
    public ResponseEntity<String> login(@RequestBody Encargado loginEncargado) {
        Optional<Encargado> encargado = encargadoService.findByEmail(loginEncargado.getEmail());

        if (encargado.isPresent() && encargadoService.verifyPassword(loginEncargado.getContrasenia(), encargado.get().getContrasenia())) {
            String estado = encargado.get().getEstado();
            String token = jwtUtil.generateToken(encargado.get().getEmail(), "encargado", encargado.get().getId());

            if (estado.equals("En revision")) {
                return ResponseEntity.ok("{\"message\": \"Su cuenta está en revisión, por favor contáctese con Soporte Tecnico\", \"token\": \"" + token + "\"}");
            }
            else if (estado.equals("Suspendido")) {
                return ResponseEntity.ok("{\"message\": \"Su cuenta ha sido suspendida, por favor contáctese con Soporte Tecnico\", \"token\": \"" + token + "\"}");
            }
            return ResponseEntity.ok("{\"token\": \"" + token + "\"}");
        } else {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
        }
    }


    @PutMapping("/update/{id}")
    public Encargado updateEncargado(@PathVariable int id, @RequestBody Encargado encargadoDetails) {
        return encargadoService.updateEncargado(id, encargadoDetails);
    }
}
