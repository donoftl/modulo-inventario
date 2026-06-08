package com.prueba.tecnica.Sof.dto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prueba.tecnica.Sof.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    // Método opcional por si en el futuro necesitas buscar una categoría directamente por su prefijo (ej: "LAP")
    Optional<Categoria> findByPrefijo(String prefijo);
    
    // Método opcional por si necesitas validar si ya existe un nombre de categoría antes de registrarlo
    boolean existsByNombre(String nombre);
}
