package com.hopematch.hopematch_backend.services;

import com.hopematch.hopematch_backend.models.Donacion;
import com.hopematch.hopematch_backend.models.Encargado;
import com.hopematch.hopematch_backend.models.Padrino;
import com.hopematch.hopematch_backend.repositories.DonacionRepository;
import com.hopematch.hopematch_backend.repositories.EncargadoRepository;
import com.hopematch.hopematch_backend.repositories.NinoRepository;
import com.hopematch.hopematch_backend.repositories.PadrinoRepository;
import com.hopematch.hopematch_backend.utils.LogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DonacionService {
    @Autowired
    private DonacionRepository donacionRepository;

    @Autowired
    private NinoRepository ninoRepository;

    @Autowired
    private PadrinoRepository padrinoRepository;

    @Autowired
    private EncargadoRepository encargadoRepository;

    public Donacion saveDonacion(Donacion donacion) {

        if (donacion.getCantidadDonacion() == null || donacion.getCantidadDonacion() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        if (donacion.getFechaDonacion() == null) {
            donacion.setFechaDonacion(LocalDate.now());
        }

        if (donacion.getPadrino() == null || donacion.getPadrino().getId() == 0) {
            throw new RuntimeException("Se requiere un padrino válido para la donación");
        } else {
            Padrino padrino = padrinoRepository.findById(donacion.getPadrino().getId())
                    .orElseThrow(() -> new RuntimeException("Padrino no encontrado con ID: " + donacion.getPadrino().getId()));
            donacion.setPadrino(padrino);
        }

        if (donacion.getEncargado() == null || donacion.getEncargado().getId() == 0) {
            throw new RuntimeException("Se requiere un encargado válido para la donación");
        } else {
            Encargado encargado = encargadoRepository.findById(donacion.getEncargado().getId())
                    .orElseThrow(() -> new RuntimeException("Encargado no encontrado con ID: " + donacion.getEncargado().getId()));
            donacion.setEncargado(encargado);
        }
        LogHelper.info("Nueva donación registrada por el padrino.");
        return donacionRepository.save(donacion);
    }

    public List<Donacion> getAllDonaciones() {
        return donacionRepository.findAll();
    }

    public List<Donacion> getDonacionesByPadrino(Long padrinoId) {
        return donacionRepository.findByPadrinoId(padrinoId);
    }

    public List<Donacion> getDonacionesByEncargado(int encargadoId) {  // Cambiado de Long a int
        return donacionRepository.findByEncargadoId(encargadoId);
    }

    public Donacion getDonacionById(int id) {
        return donacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donación no encontrada con ID: " + id));
    }

    public Donacion agregarComentarioEncargado(Integer donacionId, String comentario) {
        Donacion donacion = donacionRepository.findById(donacionId)
                .orElseThrow(() -> new RuntimeException("Donación no encontrada"));
        donacion.setComentarioEncargado(comentario);
        return donacionRepository.save(donacion);
    }

    public Donacion agregarFotoEncargado(Integer donacionId, String foto) {
        Donacion donacion = donacionRepository.findById(donacionId)
                .orElseThrow(() -> new RuntimeException("Donación no encontrada"));
        donacion.setFotoDonacion(foto);
        return donacionRepository.save(donacion);
    }
}