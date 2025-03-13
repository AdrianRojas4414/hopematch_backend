package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Nino;
import com.hopematch.hopematch_backend.services.NinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nino")
public class NinoController {

    @Autowired
    private NinoService ninoService;

    @PostMapping("/add")
    public Nino createNino(@RequestBody Nino nino) {
        return ninoService.saveNino(nino);
    }

    @GetMapping("/list")
    public List<Nino> getAllNinos() {
        return ninoService.getAllNinos();
    }

    @PutMapping("/update/{id}")
    public Nino updateNino(@PathVariable int id, @RequestBody Nino ninoDetails) {
        return ninoService.updateNino(id, ninoDetails);
    }
}
