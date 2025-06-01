package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Padrino;
import com.hopematch.hopematch_backend.repositories.PadrinoRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PadrinoService {

    @Autowired
    private PadrinoRepository padrinoRepository;


    public Padrino savePadrino(Padrino padrino){
        if (padrino.getFoto() == null || padrino.getFoto() == "") {
            padrino.setFoto("https://i.pinimg.com/736x/2c/f5/58/2cf558ab8c1f12b43f7326945672805e.jpg");
        }

        String hashedPassword = BCrypt.hashpw(padrino.getContrasenia(), BCrypt.gensalt());
        padrino.setContrasenia(hashedPassword);

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

    public Padrino updatePadrino(int id, Padrino padrinoDetails){
        if (padrinoDetails.getFoto() == null || padrinoDetails.getFoto() == "") {
            padrinoDetails.setFoto("https://i.pinimg.com/736x/2c/f5/58/2cf558ab8c1f12b43f7326945672805e.jpg");
        }
        return padrinoRepository.findById(id).map(padrino -> {
            padrino.setNombre(padrinoDetails.getNombre());
            padrino.setEmail(padrinoDetails.getEmail());

            if (padrinoDetails.getContrasenia() != null && !padrinoDetails.getContrasenia().isEmpty()) {
                String hashedPassword = BCrypt.hashpw(padrinoDetails.getContrasenia(), BCrypt.gensalt());
                padrino.setContrasenia(hashedPassword);
            }

            padrino.setCelular(padrinoDetails.getCelular());
            padrino.setFoto(padrinoDetails.getFoto());
            padrino.setEstado(padrinoDetails.getEstado());
            return padrinoRepository.save(padrino);
        }).orElseThrow(() -> new RuntimeException("Padrino no encontrado con id: " + id));
    }

    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
