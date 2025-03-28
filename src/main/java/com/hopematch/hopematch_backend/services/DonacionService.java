package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Donacion;
import com.hopematch.hopematch_backend.models.Nino;
import com.hopematch.hopematch_backend.repositories.DonacionRepository;
import com.hopematch.hopematch_backend.repositories.NinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DonacionService {
    @Autowired
    private DonacionRepository donacionRepository;

    @Autowired
    private NinoRepository ninoRepository;

    public Donacion saveDonacion(Donacion donacion) {
        if (donacion.getCantidadDonacion() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        if (donacion.getFechaDonacion() == null) {
            donacion.setFechaDonacion(java.time.LocalDate.now());
        }

        return donacionRepository.save(donacion);
    }

    public List<Donacion> getAllDonaciones() {
        return donacionRepository.findAll();
    }

}
