package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Mensaje;
import com.hopematch.hopematch_backend.repositories.MensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public List<Mensaje> getMensajesByRemitente(int idRemitente) {
        return mensajeRepository.findByIdRemitente(idRemitente);
    }

    public List<Mensaje> getMensajesByDestinatario(int idDestinatario) {
        return mensajeRepository.findByIdDestinatario(idDestinatario);
    }

    public List<Mensaje> getMensajesByDestinatarioNombre(String destinatario) {
        return mensajeRepository.findByDestinatario(destinatario);
    }

    public Mensaje updateMensaje(int id, Mensaje mensajeDetails) {
        return mensajeRepository.findById(id).map(mensaje -> {
            mensaje.setIdRemitente(mensajeDetails.getIdRemitente());
            mensaje.setIdDestinatario(mensajeDetails.getIdDestinatario());
            mensaje.setRemitente(mensajeDetails.getRemitente());
            mensaje.setDestinatario(mensajeDetails.getDestinatario());
            mensaje.setMensaje(mensajeDetails.getMensaje());
            mensaje.setLeido(mensajeDetails.isLeido());
            mensaje.setFecha(LocalDateTime.now());
            return mensajeRepository.save(mensaje);
        }).orElseThrow(() -> new RuntimeException("Mensaje no encontrado con id: " + id));
    }

    public void deleteMensaje(int id) {
        mensajeRepository.deleteById(id);
    }
}
