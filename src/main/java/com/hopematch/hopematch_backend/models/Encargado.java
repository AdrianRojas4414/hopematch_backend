package com.hopematch.hopematch_backend.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "encargados_db")
@NoArgsConstructor
@AllArgsConstructor
public class Encargado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_encargado")
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

    @Column(name = "nombre_hogar", nullable = false, length = 255)
    private String nombre_hogar;

    @Column(name = "direccion_hogar", nullable = false, length = 255)
    private String direccion_hogar;

    @OneToMany(mappedBy = "encargado", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Nino> ninos = new ArrayList<>();
}
