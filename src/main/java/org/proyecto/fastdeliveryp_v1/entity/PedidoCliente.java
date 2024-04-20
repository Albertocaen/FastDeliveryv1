package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "PEDIDO_CLIENTE", indexes = {
        @Index(name = "dni_cliente_pedido", columnList = "dni_cliente_pedido")
})
public class PedidoCliente {
    @Id
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dni_cliente_pedido")
    private Cliente dniClientePedido;

    @Column(name = "dni_repartidor_pedido", length = 20)
    private String dniRepartidorPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private Producto idProducto;

    @OneToMany(mappedBy = "idPedidoClientePp")
    private Set<PedidoClienteProducto> pedidoClienteProductos = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cliente getDniClientePedido() {
        return dniClientePedido;
    }

    public void setDniClientePedido(Cliente dniClientePedido) {
        this.dniClientePedido = dniClientePedido;
    }

    public String getDniRepartidorPedido() {
        return dniRepartidorPedido;
    }

    public void setDniRepartidorPedido(String dniRepartidorPedido) {
        this.dniRepartidorPedido = dniRepartidorPedido;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    public Set<PedidoClienteProducto> getPedidoClienteProductos() {
        return pedidoClienteProductos;
    }

    public void setPedidoClienteProductos(Set<PedidoClienteProducto> pedidoClienteProductos) {
        this.pedidoClienteProductos = pedidoClienteProductos;
    }

}