package com.hopematch.hopematch_backend.repositories;

import com.hopematch.hopematch_backend.models.Padrino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PadrinoRepository extends JpaRepository<Padrino,Integer> {
    Optional<Padrino> findByEmail(String email);
}
