package com.hopematch.hopematch_backend.repositories;

import com.hopematch.hopematch_backend.models.Nino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NinoRepository extends JpaRepository<Nino, Integer> {
    Optional<Nino> findByCi(int ci);

    @Query("SELECT DISTINCT elementos FROM Nino n JOIN n.necesidades elementos")
    List<String> findDistinctNecesidades();
}