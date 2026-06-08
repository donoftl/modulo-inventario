package com.prueba.tecnica.Sof.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categorias")
@Getter
@Setter
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long id; 

    @Column(name = "nombre_categoria", nullable = false, length = 100)
    private String nombre; 

    @Column(name = "codigo_prefijo", nullable = false, unique = true, length = 3)
    private String prefijo; 
}