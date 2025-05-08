package com.hopematch.hopematch_backend.repositories;

import com.hopematch.hopematch_backend.models.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Integer> {
    List<Visita> findByEncargadoIdAndEsperandoRespuesta(int encargadoId, boolean esperandoRespuesta);

    List<Visita> findByPadrinoId(int padrinoId);

    Optional<Visita> findByIdAndEncargadoId(int id, int encargadoId);
}
