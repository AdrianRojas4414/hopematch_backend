package com.hopematch.hopematch_backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "visitas_db")
public class Visita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "padrino_id", nullable = false)
    private Padrino padrino;

    @ManyToOne
    @JoinColumn(name = "encargado_id", nullable = false)
    private Encargado encargado;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private String hora;

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoVisita estado = EstadoVisita.PENDIENTE;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public enum EstadoVisita {
        PENDIENTE, ACEPTADA, RECHAZADA, CANCELADA
    }
}
