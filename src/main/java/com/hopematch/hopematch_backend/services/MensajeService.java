package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Mensaje;
import com.hopematch.hopematch_backend.repositories.MensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;

    public Mensaje saveMensaje(Mensaje mensaje) {
        return mensajeRepository.save(mensaje);
    }

    public List<Mensaje> getAllMensajes() {
        return mensajeRepository.findAll();
    }

    public Optional<Mensaje> getMensajeById(int id) {
        return mensajeRepository.findById(id);
    }

    public List<Mensaje> getMensajesByPadrino(int idPadrino) {
        return mensajeRepository.findByIdPadrino(idPadrino);
    }

    public List<Mensaje> getMensajesByEncargado(int idEncargado) {
        return mensajeRepository.findByIdEncargado(idEncargado);
    }

    public List<Mensaje> getMensajesByDestinatario(String destinatario) {
        return mensajeRepository.findByDestinatario(destinatario);
    }

    public Mensaje updateMensaje(int id, Mensaje mensajeDetails) {
        return mensajeRepository.findById(id).map(mensaje -> {
            mensaje.setIdPadrino(mensajeDetails.getIdPadrino());
            mensaje.setIdEncargado(mensajeDetails.getIdEncargado());
            mensaje.setDestinatario(mensajeDetails.getDestinatario());
            mensaje.setMensaje(mensajeDetails.getMensaje());
            mensaje.setFecha(mensajeDetails.getFecha());
            return mensajeRepository.save(mensaje);
        }).orElseThrow(() -> new RuntimeException("Mensaje no encontrado con id: " + id));
    }

    public void deleteMensaje(int id) {
        mensajeRepository.deleteById(id);
    }
}