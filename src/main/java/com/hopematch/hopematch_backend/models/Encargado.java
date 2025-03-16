package com.hopematch.hopematch_backend.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "encargados_db")
@NoArgsConstructor
@AllArgsConstructor
public class Encargado {
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

    @Column(name = "nombre_hogar", nullable = false, length = 255)
    private String nombre_hogar;

    @Column(name = "direccion_hogar", nullable = false, length = 255)
    private String direccion_hogar;
}
