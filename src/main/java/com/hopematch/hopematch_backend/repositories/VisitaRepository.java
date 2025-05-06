package com.hopematch.hopematch_backend.repositories;

import com.hopematch.hopematch_backend.models.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Integer> {
}
