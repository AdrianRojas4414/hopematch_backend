package com.hopematch.hopematch_backend.repositories;

import com.hopematch.hopematch_backend.models.Encargado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EncargadoRepository extends JpaRepository<Encargado, Integer> {
    Optional<Encargado> findByEmail(String email);
}
