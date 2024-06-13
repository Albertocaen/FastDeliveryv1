package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Entidad que representa un pedido de cliente en la base de datos.
 */
@Data
@Entity
@Table(name = "PEDIDO_CLIENTE")
public class PedidoCliente {
    /**
     * ID del pedido del cliente, que actúa como clave primaria.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido_cliente", nullable = false)
    private Integer id;


    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "fecha_pedido")
    private LocalDate fechaPedido;

    @Column(name = "peso")
    private Float peso;

    @Column(name = "estado", length = 50)
    private String estado;

    /**
     * Relación muchos a uno con la entidad Cliente.
     * Representa el cliente que realizó el pedido.
     */
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dni_cliente_pedido")
    private Cliente dniClientePedido;

    /**
     * Relación muchos a uno con la entidad Repartidor.
     * Representa el repartidor asignado al pedido.
     */
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dni_repartidor_pedido")
    private Repartidor dniRepartidorPedido;

    /**
     * Relación uno a muchos con la entidad PedidoClienteProducto.
     * Representa los productos asociados al pedido.
     */
    @OneToMany(mappedBy = "pedidoCliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PedidoClienteProducto> productos = new HashSet<>();
}