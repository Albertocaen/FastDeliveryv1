package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "PEDIDO_CLIENTE_PRODUCTO")
@AllArgsConstructor
@NoArgsConstructor
public class PedidoClienteProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido_cliente", nullable = false)
    private PedidoCliente pedidoCliente;

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
