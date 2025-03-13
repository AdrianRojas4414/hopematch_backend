package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Padrino;
import com.hopematch.hopematch_backend.services.PadrinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PadrinoController {

    @Autowired
    private PadrinoService padrinoService;

    @PostMapping("/addPadrino")
    public Padrino postPadrino(@RequestBody Padrino padrino){
        return padrinoService.savePadrino(padrino);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld(){
        return ResponseEntity.ok("Hello World! Esto es HOPEMATCH");
    }
}
