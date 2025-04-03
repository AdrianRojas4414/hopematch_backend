package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Donacion;
import com.hopematch.hopematch_backend.services.DonacionService;
import org.springframework.beans.factory.annotation.Autowired;
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
        if (donacion.getCantidadDonacion() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        return donacionService.saveDonacion(donacion);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Donacion>> listarDonaciones() {
        return ResponseEntity.ok(donacionService.getAllDonaciones());
    }

    @GetMapping("/by-padrino/{padrinoId}")
    public ResponseEntity<List<Donacion>> listarDonacionesPorPadrino(@PathVariable Long padrinoId) {
        return ResponseEntity.ok(donacionService.getDonacionesByPadrino(padrinoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donacion> obtenerDonacionPorId(@PathVariable int id) {
        try {
            Donacion donacion = donacionService.getDonacionById(id);
            return ResponseEntity.ok(donacion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
