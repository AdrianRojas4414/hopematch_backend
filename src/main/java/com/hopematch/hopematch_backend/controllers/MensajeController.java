package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Mensaje;
import com.hopematch.hopematch_backend.services.MensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mensajes")
@CrossOrigin(origins = "http://localhost:4200")
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;

    @PostMapping("/add")
    public Mensaje crearMensaje(@RequestBody Mensaje mensaje) {
        return mensajeService.saveMensaje(mensaje);
    }

    @GetMapping("/list")
    public List<Mensaje> listarMensajes() {
        return mensajeService.getAllMensajes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mensaje> obtenerMensajePorId(@PathVariable int id) {
        Optional<Mensaje> mensaje = mensajeService.getMensajeById(id);
        return mensaje.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/por-padrino/{idPadrino}")
    public List<Mensaje> obtenerMensajesPorPadrino(@PathVariable int idPadrino) {
        return mensajeService.getMensajesByPadrino(idPadrino);
    }

    @GetMapping("/por-encargado/{idEncargado}")
    public List<Mensaje> obtenerMensajesPorEncargado(@PathVariable int idEncargado) {
        return mensajeService.getMensajesByEncargado(idEncargado);
    }

    @GetMapping("/por-destinatario/{destinatario}")
    public List<Mensaje> obtenerMensajesPorDestinatario(@PathVariable String destinatario) {
        return mensajeService.getMensajesByDestinatario(destinatario);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Mensaje> actualizarMensaje(@PathVariable int id, @RequestBody Mensaje mensajeDetails) {
        return ResponseEntity.ok(mensajeService.updateMensaje(id, mensajeDetails));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarMensaje(@PathVariable int id) {
        mensajeService.deleteMensaje(id);
        return ResponseEntity.noContent().build();
    }
}