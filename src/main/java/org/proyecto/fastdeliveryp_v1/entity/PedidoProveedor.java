package org.proyecto.fastdeliveryp_v1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

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


}