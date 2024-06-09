package org.proyecto.fastdeliveryp_v1.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "PEDIDO_PROVEEDOR")
public class PedidoProveedor {
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor_pedido")
    private Proveedor idProveedorPedido;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dni_admin_pedido")
    private Admin dniAdminPedido;

    @OneToMany(mappedBy = "pedidoProveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PedidoProveedorProducto> productos = new HashSet<>();


}