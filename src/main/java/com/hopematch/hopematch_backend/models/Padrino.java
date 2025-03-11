package com.hopematch.hopematch_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "padrinos_db")
@NoArgsConstructor
@AllArgsConstructor
public class Padrino {
    @Id
    @GeneratedValue
    @Column(name = "id_padrino")
    private int id_padrino;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column(name = "celular", nullable = false, length = 20)
    private String celular;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado = "En revision";

    @Column(name = "foto_perfil", nullable = false, length = 255)
    private String fotoPerfil;
}
