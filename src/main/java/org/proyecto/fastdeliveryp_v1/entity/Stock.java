package org.proyecto.fastdeliveryp_v1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Entidad que representa el stock de un producto en la base de datos.
 */
@Data
@Entity
@Table(name = "STOCK")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Stock {
    /**
     * ID del stock, que actúa como clave primaria.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock", nullable = false)
    private Integer id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    /**
     * Relación muchos a uno con la entidad Producto.
     * Representa el producto al que pertenece este stock.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    @JsonBackReference
    private Producto idProducto;

    /**
     * Relación muchos a uno con la entidad Admin.
     * Representa el administrador que gestionó este stock.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DNI_ADMIN")
    @JsonBackReference
    private Admin dniAdmin;

}