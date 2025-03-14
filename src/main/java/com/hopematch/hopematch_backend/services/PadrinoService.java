package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Padrino;
import com.hopematch.hopematch_backend.repositories.PadrinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PadrinoService {

    @Autowired
    private PadrinoRepository padrinoRepository;

    public Padrino savePadrino(Padrino padrino){
        if (padrino.getFotoPerfil() == null) {
            padrino.setFotoPerfil("https://i.pinimg.com/736x/2c/f5/58/2cf558ab8c1f12b43f7326945672805e.jpg");
        }
        return padrinoRepository.save(padrino);
    }

    public Optional<Padrino> findByEmail(String email) {
        return padrinoRepository.findByEmail(email);
    }
}
