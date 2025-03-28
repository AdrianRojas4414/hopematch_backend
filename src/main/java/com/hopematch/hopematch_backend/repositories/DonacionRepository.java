package com.hopematch.hopematch_backend.repositories;

import com.hopematch.hopematch_backend.models.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonacionRepository extends JpaRepository<Donacion, Integer> {
    Optional<Donacion> findById(int id);

}
