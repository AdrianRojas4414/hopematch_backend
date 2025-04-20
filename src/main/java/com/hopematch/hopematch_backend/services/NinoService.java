package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Nino;
import com.hopematch.hopematch_backend.repositories.NinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import com.hopematch.hopematch_backend.models.Encargado;
import com.hopematch.hopematch_backend.repositories.EncargadoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NinoService {

    @Autowired
    private NinoRepository ninoRepository;
    @Autowired
    private EncargadoRepository encargadoRepository;

    public Nino saveNino(Nino nino) {
        return ninoRepository.save(nino);
    }

    public Optional<Nino> getNinoById(int id){
        return ninoRepository.findById(id);
    }

    public Optional<Nino> getNinoByCi(int ci){
        return ninoRepository.findByCi(ci);
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

    public List<String> getNecesidadesByEncargado(int idEncargado) {
        return ninoRepository.findNecesidadesByEncargado(idEncargado);
    }

    public void deleteNino(int id) {
        Optional<Nino> ninoOpt = ninoRepository.findById(id);
        if (ninoOpt.isPresent()) {
            Nino nino = ninoOpt.get();

            Encargado encargado = nino.getEncargado();
            encargado.getNinos().remove(nino);
            encargadoRepository.save(encargado);

            ninoRepository.delete(nino);
        } else {
            throw new RuntimeException("Niño no encontrado con id: " + id);
        }
    }
}
