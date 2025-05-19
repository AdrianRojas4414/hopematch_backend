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

    @Column(name = "id_padrino", nullable = false)
    private int idPadrino;

    @Column(name = "id_encargado", nullable = false)
    private int idEncargado;

    @Column(name = "remitente", length = 50, nullable = false)
    private String remitente;

    @Column(name = "destinatario", length = 50, nullable = false)
    private String destinatario;

    @Column(name = "mensaje", columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "leido", nullable = false)
    private boolean leido;

    // Constructor sin fecha y leido predeterminado a false
    public Mensaje(int idPadrino, int idEncargado, String remitente, String destinatario, String mensaje) {
        this.idPadrino = idPadrino;
        this.idEncargado = idEncargado;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
        this.leido = false;  // Por defecto el mensaje no ha sido leído
    }
}