package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Visita;
import com.hopematch.hopematch_backend.repositories.VisitaRepository;
import com.hopematch.hopematch_backend.utils.LogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class VisitaService {
    @Autowired
    private VisitaRepository visitaRepository;

    public Visita saveVisita(Visita visita) {
        if (visita.getFechaVisita() == null || visita.getFechaVisita().isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha de visita no puede ser anterior a hoy");
        }

        if (visita.getHoraVisita() == null) {
            throw new RuntimeException("Debe seleccionar una hora para la visita");
        }

        if (visita.getPadrinoId() == null || visita.getPadrinoId() == 0) {
            throw new RuntimeException("Se requiere un padrino válido para la visita");
        }

        if (visita.getEncargadoId() == null || visita.getEncargadoId() == 0) {
            throw new RuntimeException("Se requiere un encargado válido para la visita");
        }

        visita.setEstadoVisita(Visita.EstadoVisita.EN_REVISION);
        LogHelper.info("Nueva visita agendada por el padrino.");
        return visitaRepository.save(visita);
    }

    public List<Visita> getAllVisitas() {
        return visitaRepository.findAll();
    }

    public List<Visita> getVisitasByPadrino(Long padrinoId) {
        return visitaRepository.findByPadrinoId(padrinoId);
    }

    public List<Visita> getVisitasByEncargado(Integer encargadoId) {
        return visitaRepository.findByEncargadoId(encargadoId);
    }

    public Visita getVisitaById(Integer id) {
        return visitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visita no encontrada con ID: " + id));
    }

    public Visita updateEstadoVisita(Integer visitaId, Visita.EstadoVisita nuevoEstado) {
        Visita visita = getVisitaById(visitaId);
        visita.setEstadoVisita(nuevoEstado);
        return visitaRepository.save(visita);
    }
}
