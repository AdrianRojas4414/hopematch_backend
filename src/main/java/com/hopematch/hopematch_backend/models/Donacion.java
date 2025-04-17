package com.hopematch.hopematch_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @ElementCollection
    @CollectionTable(name = "donacion_fotos_progreso", joinColumns = @JoinColumn(name = "donacion_id"))
    @Column(name = "foto_url", length = 255)
    private List<String> fotosProgreso = new ArrayList<>();

    @Column(name = "fecha_donacion", nullable = false)
    private LocalDate fechaDonacion;

    @Column(name = "necesidades_seleccionadas", nullable = true)
    private String necesidadesSeleccionadas;

    @Column(name = "cantidad_donacion", nullable = false)
    private Double cantidadDonacion;

    @Column(length = 500)
    private String comentarioEncargado;

    public void agregarFotoProgreso(String fotoUrl) {
        if (fotosProgreso.size() >= 8) {
            throw new IllegalStateException("No se pueden agregar más de 8 fotos de progreso");
        }
        fotosProgreso.add(fotoUrl);
    }
}
