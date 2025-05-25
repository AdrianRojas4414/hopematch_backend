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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/visitas")
@CrossOrigin(origins = {"http://localhost:4200", "https://adrianrojas4414.github.io", "https://hopematch.publicvm.com"})
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
            @RequestBody Visita visitaRequest) {

        String token = authHeader.replace("Bearer ", "");
        int idPadrino = jwtUtil.extractId(token);

        Padrino padrino = padrinoService.getPadrinoById(idPadrino)
                .orElseThrow(() -> new RuntimeException("Padrino no encontrado"));

        Encargado encargado = encargadoService.getFirstEncargado();

        Visita visita = new Visita();
        visita.setPadrino(padrino);
        visita.setEncargado(encargado);
        visita.setFecha(visitaRequest.getFecha());
        visita.setHora(visitaRequest.getHora());
        visita.setEstado(Visita.EstadoVisita.PENDIENTE);

        visitaService.saveVisita(visita);

        return ResponseEntity.ok("Visita agendada correctamente.");
    }

    @GetMapping("/pending/encargado")
    public ResponseEntity<List<Visita>> getPendingVisitasForEncargado(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        int idEncargado = jwtUtil.extractId(token);

        List<Visita> visitasPendientes = visitaService.findByEncargadoIdAndEstado(idEncargado, Visita.EstadoVisita.PENDIENTE);
        return ResponseEntity.ok(visitasPendientes);
    }

    @PutMapping("/accept/{visitaId}")
    public ResponseEntity<String> acceptVisita(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable int visitaId) {

        String token = authHeader.replace("Bearer ", "");
        int idEncargado = jwtUtil.extractId(token);

        Optional<Visita> optionalVisita = visitaService.findByIdAndEncargadoId(visitaId, idEncargado);

        if (optionalVisita.isPresent()) {
            Visita visita = optionalVisita.get();
            if (visita.getEstado() == Visita.EstadoVisita.PENDIENTE) {
                visita.setEstado(Visita.EstadoVisita.ACEPTADA);
                visitaService.saveVisita(visita);
                return ResponseEntity.ok("Visita aceptada correctamente.");
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("No se pudo aceptar la visita. Puede que no exista, no te pertenezca o no esté pendiente.");
    }

    @PutMapping("/deny/{visitaId}")
    public ResponseEntity<String> denyVisita(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable int visitaId) {

        String token = authHeader.replace("Bearer ", "");
        int idEncargado = jwtUtil.extractId(token);

        Optional<Visita> optionalVisita = visitaService.findByIdAndEncargadoId(visitaId, idEncargado);

        if (optionalVisita.isPresent()) {
            Visita visita = optionalVisita.get();
            if (visita.getEstado() == Visita.EstadoVisita.PENDIENTE) {
                visita.setEstado(Visita.EstadoVisita.RECHAZADA);
                visitaService.saveVisita(visita);
                return ResponseEntity.ok("Visita rechazada correctamente.");
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("No se pudo rechazar la visita. Puede que no exista, no te pertenezca o no esté pendiente.");
    }

    @GetMapping("/mine/padrino")
    public ResponseEntity<List<Visita>> getMyVisitasForPadrino(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        int idPadrino = jwtUtil.extractId(token);

        List<Visita> misVisitas = visitaService.findByPadrinoId(idPadrino);
        return ResponseEntity.ok(misVisitas);
    }

    @GetMapping("/horarios-disponibles")
    public ResponseEntity<List<String>> getHorariosDisponibles() {
        List<String> horarios = Arrays.asList(
                "10:00 AM", "11:00 AM", "12:00 PM",
                "2:00 PM", "3:00 PM", "4:00 PM"
        );
        return ResponseEntity.ok(horarios);
    }
}