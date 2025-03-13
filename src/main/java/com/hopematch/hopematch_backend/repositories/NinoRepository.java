package com.hopematch.hopematch_backend.repositories;

import com.hopematch.hopematch_backend.models.Nino;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NinoRepository extends JpaRepository<Nino, Integer> {
}