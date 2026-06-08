package com.prueba.tecnica.Sof.dto.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prueba.tecnica.Sof.model.Activo;

@Repository
public interface ActivoRepository extends JpaRepository<Activo, UUID>, JpaSpecificationExecutor<Activo> {
    
    // Cuenta cuántos activos de una categoría específica se crearon en un año dado
    @Query("SELECT COUNT(a) FROM Activo a WHERE a.categoria.id = :idCategoria AND FUNCTION('YEAR', a.fechaIngreso) = :anio")
    long countByCategoriaAndAnio(@Param("idCategoria") Long idCategoria, @Param("anio") int anio);
    
    boolean existsByNumeroSerie(String numeroSerie);
}
