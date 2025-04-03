package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Encargado;
import com.hopematch.hopematch_backend.models.Nino;
import com.hopematch.hopematch_backend.services.NinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/nino")
@CrossOrigin("http://localhost:4200/")
public class NinoController {

    @Autowired
    private NinoService ninoService;

    @PostMapping("/add")
    public Nino createNino(@RequestBody Nino nino) {
        return ninoService.saveNino(nino);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nino> getNinoById(@PathVariable int id){
        Optional<Nino> nino = ninoService.getNinoById(id);
        return new ResponseEntity<>(nino.get(), HttpStatus.OK);
    }

    @GetMapping("/ci/{ci}")
    public ResponseEntity<Nino> getNinoByCi(@PathVariable int ci){
        Optional<Nino> nino = ninoService.getNinoByCi(ci);
        return new ResponseEntity<>(nino.get(), HttpStatus.OK);
    }

    @GetMapping("/list")
    public List<Nino> getAllNinos() {
        return ninoService.getAllNinos();
    }

    @PutMapping("/update/{id}")
    public Nino updateNino(@PathVariable int id, @RequestBody Nino ninoDetails) {
        return ninoService.updateNino(id, ninoDetails);
    }

    @GetMapping("/necesidades/{idEncargado}")
    public ResponseEntity<List<String>> getNecesidadesByEncargado(@PathVariable int idEncargado) {
        return ResponseEntity.ok(ninoService.getNecesidadesByEncargado(idEncargado));
    }

}
