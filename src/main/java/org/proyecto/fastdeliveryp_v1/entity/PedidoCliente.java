package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "PEDIDO_CLIENTE")
public class PedidoCliente {
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

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dni_cliente_pedido")
    private Cliente dniClientePedido;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dni_repartidor_pedido")
    private Repartidor dniRepartidorPedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto")
    private Producto producto;


}