package org.proyecto.fastdeliveryp_v1.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
/**
 * Entidad que representa un pedido a un proveedor en la base de datos.
 */
@Data
@Entity
@Table(name = "PEDIDO_PROVEEDOR")
public class PedidoProveedor {
    /**
     * ID del pedido al proveedor, que actúa como clave primaria.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido_proveedor", nullable = false)
    private Integer id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "fecha_pedido")
    private LocalDate fechaPedido;

    @Column(name = "estado", length = 50)
    private String estado;

    /**
     * Relación muchos a uno con la entidad Proveedor.
     * Representa el proveedor al que se le hizo el pedido.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor_pedido")
    private Proveedor idProveedorPedido;

    /**
     * Relación muchos a uno con la entidad Admin.
     * Representa el administrador que hizo el pedido.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dni_admin_pedido")
    private Admin dniAdminPedido;
    /**
     * Relación uno a muchos con la entidad PedidoProveedorProducto.
     * Representa los productos asociados al pedido.
     */
    @OneToMany(mappedBy = "pedidoProveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PedidoProveedorProducto> productos = new HashSet<>();


}