package com.hopematch.hopematch_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "visitas_db")
@NoArgsConstructor
@AllArgsConstructor
public class Visita {
    public enum EstadoVisita {
        EN_REVISION,
        ACEPTADA,
        RECHAZADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_visita")
    private int id;

    @Column(name = "padrino_id", nullable = false)
    private Long padrinoId;

    @Column(name = "encargado_id", nullable = false)
    private Integer encargadoId;

    @Column(name = "fecha_visita", nullable = false)
    private LocalDate fechaVisita;

    @Column(name = "hora_visita", nullable = false)
    private LocalTime horaVisita;

    @Column(name = "estado_visita", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoVisita estadoVisita = EstadoVisita.EN_REVISION;
}
