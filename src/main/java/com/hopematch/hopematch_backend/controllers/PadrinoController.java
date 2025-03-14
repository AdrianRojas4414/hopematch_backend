package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Padrino;
import com.hopematch.hopematch_backend.services.PadrinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/padrino")
@CrossOrigin("http://localhost:4200/")
public class PadrinoController {

    @Autowired
    private PadrinoService padrinoService;

    @PostMapping("/add")
    public Padrino postPadrino(@RequestBody Padrino padrino){
        return padrinoService.savePadrino(padrino);
    }

    @GetMapping("/list")
    public List<Padrino> getAllPadrinos(){
        return padrinoService.getAllPadrinos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Padrino> getPadrinoById(@PathVariable int id){
        Optional<Padrino> padrino = padrinoService.getPadrinoById(id);
        return new ResponseEntity<>(padrino.get(), HttpStatus.OK);
    }
}
