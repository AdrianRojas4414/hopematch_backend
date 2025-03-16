package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Nino;
import com.hopematch.hopematch_backend.repositories.NinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NinoService {

    @Autowired
    private NinoRepository ninoRepository;

    public Nino saveNino(Nino nino) {
        return ninoRepository.save(nino);
    }

    public Optional<Nino> getNinoById(int id){
        return ninoRepository.findById(id);
    }

    public List<Nino> getAllNinos() {
        return ninoRepository.findAll();
    }

    public Nino updateNino(int id, Nino ninoDetails) {
        return ninoRepository.findById(id).map(nino -> {
            nino.setCi(ninoDetails.getCi());
            nino.setNombre(ninoDetails.getNombre());
            nino.setFechaNacimiento(ninoDetails.getFechaNacimiento());
            nino.setNecesidades(ninoDetails.getNecesidades());
            return ninoRepository.save(nino);
        }).orElseThrow(() -> new RuntimeException("Niño no encontrado con id: " + id));
    }

}
