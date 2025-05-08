package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Visita;
import com.hopematch.hopematch_backend.repositories.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VisitaService {

    @Autowired
    private VisitaRepository visitaRepository;

    public Visita saveVisita(Visita visita) {
        return visitaRepository.save(visita);
    }

    public List<Visita> getPendingVisitasForEncargado(int encargadoId) {
        return visitaRepository.findByEncargadoIdAndEstado(encargadoId, Visita.EstadoVisita.PENDIENTE);
    }

    public Optional<Visita> findByIdAndEncargadoId(int visitaId, int encargadoId) {
        return visitaRepository.findByIdAndEncargadoId(visitaId, encargadoId);
    }

    public List<Visita> findByPadrinoId(int padrinoId) {
        return visitaRepository.findByPadrinoId(padrinoId);
    }

    public List<Visita> findByEncargadoIdAndEstado(int encargadoId, Visita.EstadoVisita estado) {
        return visitaRepository.findByEncargadoIdAndEstado(encargadoId, estado);
    }

    @Transactional
    public Optional<Visita> acceptVisita(int visitaId, int encargadoId) {
        Optional<Visita> optionalVisita = this.findByIdAndEncargadoId(visitaId, encargadoId);

        if (optionalVisita.isPresent()) {
            Visita visita = optionalVisita.get();
            if (visita.getEstado() == Visita.EstadoVisita.PENDIENTE) {
                visita.setEstado(Visita.EstadoVisita.ACEPTADA);
                return Optional.of(visitaRepository.save(visita));
            }
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<Visita> denyVisita(int visitaId, int encargadoId) {
        Optional<Visita> optionalVisita = this.findByIdAndEncargadoId(visitaId, encargadoId);

        if (optionalVisita.isPresent()) {
            Visita visita = optionalVisita.get();
            if (visita.getEstado() == Visita.EstadoVisita.PENDIENTE) {
                visita.setEstado(Visita.EstadoVisita.RECHAZADA);
                return Optional.of(visitaRepository.save(visita));
            }
        }
        return Optional.empty();
    }
}
