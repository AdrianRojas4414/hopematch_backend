package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Encargado;
import com.hopematch.hopematch_backend.models.Nino;
import com.hopematch.hopematch_backend.repositories.EncargadoRepository;
import com.hopematch.hopematch_backend.repositories.NinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
            encargado.setContrasenia(encargadoDetails.getContrasenia());
            encargado.setNombre_hogar(encargadoDetails.getNombre_hogar());
            encargado.setDireccion_hogar(encargadoDetails.getDireccion_hogar());

            return encargadoRepository.save(encargado);
        } else {
            throw new RuntimeException("Encargado con ID " + id + " no encontrado.");
        }
    }


    public Map<String, Integer> getNecesidadesAgrupadas(int idEncargado) {
        Optional<Encargado> encargado = encargadoRepository.findById(idEncargado);
        if (encargado.isEmpty()) {
            throw new RuntimeException("Encargado no encontrado");
        }

        Map<String, Integer> necesidadesCount = new HashMap<>();
        for (Nino nino : encargado.get().getNinos()) {
            for (String necesidad : nino.getNecesidades()) {
                necesidadesCount.put(necesidad, necesidadesCount.getOrDefault(necesidad, 0) + 1);
            }
        }

        return necesidadesCount.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}