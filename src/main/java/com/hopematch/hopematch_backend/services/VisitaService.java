package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Visita;
import com.hopematch.hopematch_backend.repositories.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitaService {

    @Autowired
    private VisitaRepository visitaRepository;

    public Visita saveVisita(Visita visita) {
        return visitaRepository.save(visita);
    }
}
