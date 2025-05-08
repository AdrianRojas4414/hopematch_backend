package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Visita;
import com.hopematch.hopematch_backend.models.Padrino;
import com.hopematch.hopematch_backend.models.Encargado;
import com.hopematch.hopematch_backend.services.PadrinoService;
import com.hopematch.hopematch_backend.services.EncargadoService;
import com.hopematch.hopematch_backend.services.VisitaService;
import com.hopematch.hopematch_backend.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/visitas")
@CrossOrigin(origins = {"http://localhost:4200", "https://adrianrojas4414.github.io"})
public class VisitaController {

    @Autowired
    private VisitaService visitaService;

    @Autowired
    private PadrinoService padrinoService;

    @Autowired
    private EncargadoService encargadoService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/agendar")
    public ResponseEntity<String> agendarVisita(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Visita visita) {

        String token = authHeader.replace("Bearer ", "");
        int idPadrino = jwtUtil.extractId(token);

        Padrino padrino = padrinoService.getPadrinoById(idPadrino)
                .orElseThrow(() -> new RuntimeException("Padrino no encontrado"));

        Encargado encargado = encargadoService.getFirstEncargado();

        visita.setPadrino(padrino);
        visita.setEncargado(encargado);
        visita.setEsperandoRespuesta(true);

        visitaService.saveVisita(visita);

        return ResponseEntity.ok("Visita agendada correctamente.");
    }

    @GetMapping("/pending/encargado")
    public ResponseEntity<List<Visita>> getPendingVisitasForEncargado(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        int idEncargado = jwtUtil.extractId(token);

        List<Visita> visitasPendientes = visitaService.getPendingVisitasForEncargado(idEncargado);
        return ResponseEntity.ok(visitasPendientes);
    }

    @PutMapping("/accept/{visitaId}")
    public ResponseEntity<String> acceptVisita(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable int visitaId) {

        String token = authHeader.replace("Bearer ", "");
        int idEncargado = jwtUtil.extractId(token);

        Optional<Visita> updatedVisita = visitaService.updateVisitaStatus(visitaId, idEncargado, false); // 'false' = Acción tomada

        if (updatedVisita.isPresent()) {

            return ResponseEntity.ok("Visita aceptada (estado actualizado a no pendiente).");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo aceptar la visita. Puede que no exista, no te pertenezca o no esté pendiente.");
        }
    }

    @PutMapping("/deny/{visitaId}")
    public ResponseEntity<String> denyVisita(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable int visitaId) {

        String token = authHeader.replace("Bearer ", "");
        int idEncargado = jwtUtil.extractId(token);

        Optional<Visita> updatedVisita = visitaService.updateVisitaStatus(visitaId, idEncargado, false); // 'false' = Acción tomada

        if (updatedVisita.isPresent()) {

            return ResponseEntity.ok("Visita denegada (estado actualizado a no pendiente).");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo denegar la visita. Puede que no exista, no te pertenezca o no esté pendiente.");
        }
    }

    @GetMapping("/mine/padrino")
    public ResponseEntity<List<Visita>> getMyVisitasForPadrino(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        int idPadrino = jwtUtil.extractId(token);

        List<Visita> misVisitas = visitaService.getVisitasForPadrino(idPadrino);

        return ResponseEntity.ok(misVisitas);
    }
}
