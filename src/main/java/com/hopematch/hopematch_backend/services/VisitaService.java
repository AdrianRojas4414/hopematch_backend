package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Visita;
import com.hopematch.hopematch_backend.repositories.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importar Transactional

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
        return visitaRepository.findByEncargadoIdAndEsperandoRespuesta(encargadoId, true);
    }

    public Optional<Visita> getVisitaByIdAndEncargadoId(int visitaId, int encargadoId) {
        return visitaRepository.findByIdAndEncargadoId(visitaId, encargadoId);
    }

    @Transactional
    public Optional<Visita> updateVisitaStatus(int visitaId, int encargadoId, boolean nuevoEstadoEspera) {
        Optional<Visita> optionalVisita = visitaRepository.findByIdAndEncargadoId(visitaId, encargadoId);

        if (optionalVisita.isPresent()) {
            Visita visita = optionalVisita.get();
            if (visita.isEsperandoRespuesta()) {
                visita.setEsperandoRespuesta(nuevoEstadoEspera);
                return Optional.of(visitaRepository.save(visita));
            }
        }
        return Optional.empty();
    }

    public List<Visita> getVisitasForPadrino(int padrinoId) {
        return visitaRepository.findByPadrinoId(padrinoId);
    }
}
