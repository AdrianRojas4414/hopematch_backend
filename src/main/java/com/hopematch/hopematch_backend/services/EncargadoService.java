package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Encargado;
import com.hopematch.hopematch_backend.models.Nino;
import com.hopematch.hopematch_backend.repositories.EncargadoRepository;
import com.hopematch.hopematch_backend.repositories.NinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EncargadoService {
    @Autowired
    private EncargadoRepository encargadoRepository;

    @Autowired
    private NinoRepository ninoRepository;

    public Encargado saveEncargado(Encargado encargado){
        return encargadoRepository.save(encargado);
    }

    public List<Encargado> getAllEncargados(){
        return encargadoRepository.findAll();
    }

    public Optional<Encargado> getEncargadoById(int id){
        return encargadoRepository.findById(id);
    }

    public Optional<Encargado> findByEmail(String email){
        return encargadoRepository.findByEmail(email);
    }

    public Nino addNinoToEncargado(int idEncargado, Nino nino){
        Optional<Encargado> optionalEncargado = encargadoRepository.findById(idEncargado);
        if (optionalEncargado.isPresent()){
            Encargado encargado = optionalEncargado.get();
            nino.setEncargado(encargado);
            ninoRepository.save(nino);
            return nino;
        }
        throw new RuntimeException("Encargado no Valido");
    }


}
