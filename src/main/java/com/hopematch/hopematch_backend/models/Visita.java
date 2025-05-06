package com.hopematch.hopematch_backend.models;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "esperando_respuesta", nullable = false)
    private boolean esperandoRespuesta = true;
}
