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
@CrossOrigin(origins = {"http://localhost:4200", "https://adrianrojas4414.github.io", "https://hopematch.publicvm.com", "https://hopematch.netlify.app"})
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

    @GetMapping("/encargado/{idEncargado}")
    public ResponseEntity<List<Nino>> getNinosByEncargado(@PathVariable int idEncargado) {
        List<Nino> ninos = ninoService.getNinosbyEncargado(idEncargado);
        return new ResponseEntity<>(ninos, HttpStatus.OK);
    }

    @GetMapping("/necesidades/{idEncargado}")
    public ResponseEntity<List<String>> getNecesidadesByEncargado(@PathVariable int idEncargado) {
        return ResponseEntity.ok(ninoService.getNecesidadesByEncargado(idEncargado));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteNino(@PathVariable int id) {
        try {
            ninoService.deleteNino(id);
            return ResponseEntity.ok("Niño eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el niño: " + e.getMessage());
        }
    }

}
