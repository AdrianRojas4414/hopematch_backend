package com.hopematch.hopematch_backend.repositories;

import com.hopematch.hopematch_backend.models.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {
    // Ejemplo de método de consulta: buscar mensajes por destinatario
    List<Mensaje> findByDestinatario(String destinatario);

    // Si necesitas filtrar por padrino o encargado
    List<Mensaje> findByIdPadrino(int idPadrino);
    List<Mensaje> findByIdEncargado(int idEncargado);
}
