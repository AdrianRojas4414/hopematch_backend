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
}
