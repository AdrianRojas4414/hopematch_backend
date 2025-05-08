package com.hopematch.hopematch_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mensajes")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_padrino")
    private int idPadrino;

    @Column(name = "id_encargado")
    private int idEncargado;

    @Column(name = "destinatario", length = 50)
    private String destinatario;

    @Column(name = "mensaje", columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "fecha", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fecha;

    // Opcional: puedes generar un constructor sin `fecha` si prefieres que se asigne automáticamente
    public Mensaje(int idPadrino, int idEncargado, String destinatario, String mensaje) {
        this.idPadrino = idPadrino;
        this.idEncargado = idEncargado;
        this.destinatario = destinatario;
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
    }
}
