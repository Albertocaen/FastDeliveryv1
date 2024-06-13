package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad que representa la relación entre un pedido de cliente y un producto en la base de datos.
 */
@Data
@Entity
@Table(name = "PEDIDO_CLIENTE_PRODUCTO")
@AllArgsConstructor
@NoArgsConstructor
public class PedidoClienteProducto {
    /**
     * ID de la relación entre pedido de cliente y producto, que actúa como clave primaria.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    /**
     * Relación muchos a uno con la entidad PedidoCliente.
     * Representa el pedido al que pertenece esta relación.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido_cliente", nullable = false)
    private PedidoCliente pedidoCliente;
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
