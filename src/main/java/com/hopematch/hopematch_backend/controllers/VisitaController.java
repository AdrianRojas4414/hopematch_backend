package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Visita;
import com.hopematch.hopematch_backend.services.VisitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/visitas")
@CrossOrigin(origins = {"http://localhost:4200", "https://adrianrojas4414.github.io", "https://hopematch.publicvm.com", "https://hopematch.netlify.app"})
public class VisitaController {

    @Autowired
    private VisitaService visitaService;

    @PostMapping("/agendar")
    public ResponseEntity<?> agendarVisita(@RequestBody Map<String, Object> request) {
        try {
            Visita visita = new Visita();
            visita.setFechaVisita(LocalDate.parse(request.get("fechaVisita").toString()));
            visita.setHoraVisita(LocalTime.parse(request.get("horaVisita").toString()));
            visita.setPadrinoId(Long.parseLong(request.get("padrinoId").toString()));
            visita.setEncargadoId(Integer.parseInt(request.get("encargadoId").toString()));

            Visita visitaGuardada = visitaService.saveVisita(visita);
            return ResponseEntity.ok(visitaGuardada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("Ocurrió un error interno: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Visita>> listarVisitas() {
        return ResponseEntity.ok(visitaService.getAllVisitas());
    }

    @GetMapping("/by-padrino/{padrinoId}")
    public ResponseEntity<List<Visita>> listarVisitasPorPadrino(@PathVariable Long padrinoId) {
        return ResponseEntity.ok(visitaService.getVisitasByPadrino(padrinoId));
    }

    @GetMapping("/by-encargado/{encargadoId}")
    public ResponseEntity<List<Visita>> listarVisitasPorEncargado(@PathVariable Integer encargadoId) {
        return ResponseEntity.ok(visitaService.getVisitasByEncargado(encargadoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Visita> obtenerVisitaPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(visitaService.getVisitaById(id));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Visita> actualizarEstadoVisita(
            @PathVariable Integer id,
            @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(visitaService.updateEstadoVisita(id,
                Visita.EstadoVisita.valueOf(request.get("estado"))));
    }
}
