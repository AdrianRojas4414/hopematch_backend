package com.hopematch.hopematch_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "donaciones_db")
@NoArgsConstructor
@AllArgsConstructor
public class Donacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_donacion")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "padrino_id")
    private Padrino padrino;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "encargado_id")
    private Encargado encargado;

    @Column(name = "foto_donacion", length = 255, nullable = true)
    private String fotoDonacion;

    @Column(name = "fecha_donacion", nullable = false)
    private LocalDate fechaDonacion;

    @Column(name = "necesidades_seleccionadas", nullable = true)
    private String necesidadesSeleccionadas;

    @Column(name = "cantidad_donacion", nullable = false)
    private Double cantidadDonacion;

}
