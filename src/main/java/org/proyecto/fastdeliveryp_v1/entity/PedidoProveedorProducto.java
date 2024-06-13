package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa la relación entre un pedido a un proveedor y un producto en la base de datos.
 */
@Data
@Entity
@Table(name = "PEDIDO_PROVEEDOR_PRODUCTO")
@AllArgsConstructor
@NoArgsConstructor
public class PedidoProveedorProducto {
    /**
     * ID de la relación entre pedido a proveedor y producto, que actúa como clave primaria.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * Relación muchos a uno con la entidad PedidoProveedor.
     * Representa el pedido al proveedor al que pertenece esta relación.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido_proveedor", nullable = false)
    private PedidoProveedor pedidoProveedor;

    /**
     * Relación muchos a uno con la entidad Producto.
     * Representa el producto al que pertenece esta relación.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private float precio;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 255)
    private String comentarios;
}
