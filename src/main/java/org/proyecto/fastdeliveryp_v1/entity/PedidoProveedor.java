package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "PEDIDO_PROVEEDOR", indexes = {
        @Index(name = "id_proveedor_pedido", columnList = "id_proveedor_pedido"),
        @Index(name = "dni_admin_pedido", columnList = "dni_admin_pedido")
})
public class PedidoProveedor {
    @Id
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

    @OneToMany(mappedBy = "idPedidoProveedorPp")
    private Set<PedidoProveedorProducto> pedidoProveedorProductos = new LinkedHashSet<>();

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Proveedor getIdProveedorPedido() {
        return idProveedorPedido;
    }

    public void setIdProveedorPedido(Proveedor idProveedorPedido) {
        this.idProveedorPedido = idProveedorPedido;
    }

    public Admin getDniAdminPedido() {
        return dniAdminPedido;
    }

    public void setDniAdminPedido(Admin dniAdminPedido) {
        this.dniAdminPedido = dniAdminPedido;
    }

    public Set<PedidoProveedorProducto> getPedidoProveedorProductos() {
        return pedidoProveedorProductos;
    }

    public void setPedidoProveedorProductos(Set<PedidoProveedorProducto> pedidoProveedorProductos) {
        this.pedidoProveedorProductos = pedidoProveedorProductos;
    }

}