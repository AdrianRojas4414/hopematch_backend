package com.hopematch.hopematch_backend.repositories;

import com.hopematch.hopematch_backend.models.Donacion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonacionRepository extends JpaRepository<Donacion, Integer> {
    @Query("SELECT d FROM Donacion d LEFT JOIN FETCH d.padrino LEFT JOIN FETCH d.encargado WHERE d.id = :id")
    Optional<Donacion> findByIdWithRelations(@Param("id") Integer id);

    @Override
    @EntityGraph(attributePaths = {"padrino", "encargado"})
    List<Donacion> findAll();

    Optional<Donacion> findById(int id);

}
