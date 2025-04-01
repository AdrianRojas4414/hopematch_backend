package com.hopematch.hopematch_backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "padrinos_db")
@NoArgsConstructor
@AllArgsConstructor
public class Padrino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_padrino")
    private int id;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column(name = "celular", nullable = false, length = 20)
    private int celular;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado = "En revision";

    @Column(name = "foto_perfil", length = 255)
    private String foto;

    @Column(name = "contrasenia", nullable = false, length = 255)
    private String contrasenia;

    @OneToMany(mappedBy = "padrino", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Donacion> donaciones = new ArrayList<>();

    public int getId() {
        return id;
    }
}
