package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Padrino;
import com.hopematch.hopematch_backend.repositories.PadrinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PadrinoService {

    @Autowired
    private PadrinoRepository padrinoRepository;

    public Padrino savePadrino(Padrino padrino){
        if (padrino.getFoto() == null) {
            padrino.setFoto("https://i.pinimg.com/736x/2c/f5/58/2cf558ab8c1f12b43f7326945672805e.jpg");
        }
        return padrinoRepository.save(padrino);
    }

    public List<Padrino> getAllPadrinos(){
        return padrinoRepository.findAll();
    }

    public Optional<Padrino> getPadrinoById(int id){
         return padrinoRepository.findById(id);
    }

    public Optional<Padrino> findByEmail(String email) {
        return padrinoRepository.findByEmail(email);
    }
}
