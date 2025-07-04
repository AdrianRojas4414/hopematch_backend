package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Donacion;
import com.hopematch.hopematch_backend.services.DonacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/donaciones")
@CrossOrigin(origins = {"http://localhost:4200", "https://adrianrojas4414.github.io", "https://hopematch.publicvm.com", "https://hopematch.netlify.app"})
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

    @GetMapping("/by-encargado/{encargadoId}")
    public ResponseEntity<List<Donacion>> listarDonacionesPorEncargado(@PathVariable int encargadoId) {
        return ResponseEntity.ok(donacionService.getDonacionesByEncargado(encargadoId));
    }

    @PostMapping("/{id}/comentario")
    public ResponseEntity<Donacion> agregarComentario(
            @PathVariable Integer id,
            @RequestBody String comentario) {
        return ResponseEntity.ok(donacionService.agregarComentarioEncargado(id, comentario));
    }

    @PutMapping("/{id}/foto")
    public ResponseEntity<Donacion> agregarFoto(
            @PathVariable Integer id,
            @RequestBody Map<String, String> request) {
        String fotoUrl = request.get("fotoUrl");
        return ResponseEntity.ok(donacionService.agregarFotoEncargado(id, fotoUrl));
    }

    @PostMapping("/{id}/fotos-progreso")
    public ResponseEntity<Donacion> agregarFotosProgreso(
            @PathVariable Integer id,
            @RequestBody Map<String, List<String>> request) {
        List<String> fotos = request.get("fotos");
        return ResponseEntity.ok(donacionService.agregarFotosProgreso(id, fotos));
    }

    @DeleteMapping("/{id}/fotos-progreso/{index}")
    public ResponseEntity<Donacion> eliminarFotoProgreso(
            @PathVariable Integer id,
            @PathVariable int index) {
        return ResponseEntity.ok(donacionService.eliminarFotoProgreso(id, index));
    }

    @PutMapping("/{id}/fotos-progreso")
    public ResponseEntity<Donacion> actualizarFotosProgreso(
            @PathVariable Integer id,
            @RequestBody Map<String, List<String>> request) {
        List<String> fotos = request.get("fotos");
        return ResponseEntity.ok(donacionService.actualizarFotosProgreso(id, fotos));
    }
}
