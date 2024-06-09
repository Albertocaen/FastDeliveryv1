package org.proyecto.fastdeliveryp_v1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "STOCK")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock", nullable = false)
    private Integer id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    @JsonBackReference
    private Producto idProducto;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DNI_ADMIN")
    @JsonBackReference
    private Admin dniAdmin;

}