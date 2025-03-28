package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Donacion;
import com.hopematch.hopematch_backend.services.DonacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/donaciones")
@CrossOrigin(origins = "http://localhost:4200")
public class DonacionController {

    @Autowired
    private DonacionService donacionService;

    @PostMapping("/add")
    public Donacion agregarDonacion(@RequestBody Donacion donacion) {
        if (donacion.getFechaDonacion() == null) {
            donacion.setFechaDonacion(LocalDate.now());
        }

        return donacionService.saveDonacion(donacion);
    }


    @GetMapping("/listar")
    public ResponseEntity<List<Donacion>> listarDonaciones() {
        return ResponseEntity.ok(donacionService.getAllDonaciones());
    }

}
