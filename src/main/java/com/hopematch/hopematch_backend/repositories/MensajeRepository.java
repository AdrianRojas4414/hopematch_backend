package com.hopematch.hopematch_backend.repositories;

import com.hopematch.hopematch_backend.models.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {
    // Buscar mensajes por nombre del destinatario
    List<Mensaje> findByDestinatario(String destinatario);

    // Filtrar por id del remitente
    List<Mensaje> findByIdRemitente(int idRemitente);

    // Filtrar por id del destinatario
    List<Mensaje> findByIdDestinatario(int idDestinatario);
}
