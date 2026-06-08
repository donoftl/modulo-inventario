package com.prueba.tecnica.Sof.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "activos_tecnologicos")
@Getter
@Setter
public class Activo {

    @Id
    @Column(name = "id_tecnico", length = 36)
    private UUID id;

    @Column(name = "folio_inventario", nullable = false, unique = true, length = 50)
    private String folio; 

    @Column(name = "numero_serie", nullable = false, unique = true, length = 100)
    private String numeroSerie; 

    @Column(name = "marca_modelo", nullable = false)
    private String marcaModelo; 

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(name = "costo_adquisicion", nullable = false, precision = 12, scale = 2)
    private BigDecimal costo;

    @Column(name = "fecha_ingreso")
    private LocalDateTime fechaIngreso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria; 

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID(); // Garantiza el UUID único técnico
        }
        if (this.fechaIngreso == null) {
            this.fechaIngreso = LocalDateTime.now(); 
        }
    }
}