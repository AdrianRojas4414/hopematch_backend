package com.hopematch.hopematch_backend.controllers;


import com.hopematch.hopematch_backend.models.Encargado;
import com.hopematch.hopematch_backend.models.Nino;
import com.hopematch.hopematch_backend.services.EncargadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/encargado")
@CrossOrigin("http://localhost:4200/")
public class EncargadoController {

    @Autowired
    private EncargadoService encargadoService;

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
        if (encargado.isPresent() && loginEncargado.getContrasenia().equals(encargado.get().getContrasenia())) {
            return ResponseEntity.ok("{\"id\": \"" + encargado.get().getId() + "\", \"userType\": \"Encargado\"}");
        } else {
            return ResponseEntity.status(401).body("{\"message\": \"Usuario o contraseña incorrectos\"}");
        }
    }

    @PutMapping("/update/{id}")
    public Encargado updateEncargado(@PathVariable int id, @RequestBody Encargado encargadoDetails) {
        return encargadoService.updateEncargado(id, encargadoDetails);
    }
}
