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
        if (encargado.getFoto() == null || encargado.getFoto() == "") {
            encargado.setFoto("https://i.pinimg.com/736x/2c/f5/58/2cf558ab8c1f12b43f7326945672805e.jpg");
        }
        if (encargado.getFoto_hogar() == null || encargado.getFoto_hogar() == "") {
            encargado.setFoto_hogar("https://static.vecteezy.com/system/resources/previews/004/550/083/non_2x/houses-logo-illustration-free-vector.jpg");
        }
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


    public Encargado updateEncargado(int id, Encargado encargadoDetails) {
        Optional<Encargado> optionalEncargado = encargadoRepository.findById(id);

        if (optionalEncargado.isPresent()) {
            Encargado encargado = optionalEncargado.get();

            encargado.setNombre(encargadoDetails.getNombre());
            encargado.setCelular(encargadoDetails.getCelular());
            encargado.setEmail(encargadoDetails.getEmail());
            encargado.setFoto(encargadoDetails.getFoto());
            encargado.setFoto_hogar(encargadoDetails.getFoto_hogar());
            encargado.setContrasenia(encargadoDetails.getContrasenia());
            encargado.setNombre_hogar(encargadoDetails.getNombre_hogar());
            encargado.setDireccion_hogar(encargadoDetails.getDireccion_hogar());
            encargado.setEstado(encargadoDetails.getEstado());
            encargado.setDescripcion(encargadoDetails.getDescripcion());

            return encargadoRepository.save(encargado);
        } else {
            throw new RuntimeException("Encargado con ID " + id + " no encontrado.");
        }
    }

    public Encargado getFirstEncargado() {
        return encargadoRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay encargados disponibles"));
    }
}
