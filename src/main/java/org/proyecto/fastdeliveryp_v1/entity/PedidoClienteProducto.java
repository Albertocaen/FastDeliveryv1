package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "PEDIDO_CLIENTE_PRODUCTO", indexes = {
        @Index(name = "id_pedido_cliente_pp", columnList = "id_pedido_cliente_pp"),
        @Index(name = "id_producto_pp", columnList = "id_producto_pp")
})
public class PedidoClienteProducto {
    @Id
    @Column(name = "id_pedido_cliente_producto", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido_cliente_pp")
    private PedidoCliente idPedidoClientePp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto_pp")
    private Producto idProductoPp;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio_unitario")
    private Float precioUnitario;

    @Column(name = "subtotal")
    private Float subtotal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PedidoCliente getIdPedidoClientePp() {
        return idPedidoClientePp;
    }

    public void setIdPedidoClientePp(PedidoCliente idPedidoClientePp) {
        this.idPedidoClientePp = idPedidoClientePp;
    }

    public Producto getIdProductoPp() {
        return idProductoPp;
    }

    public void setIdProductoPp(Producto idProductoPp) {
        this.idProductoPp = idProductoPp;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Float subtotal) {
        this.subtotal = subtotal;
    }

}