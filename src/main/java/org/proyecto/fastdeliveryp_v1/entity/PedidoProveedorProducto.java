package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "PEDIDO_PROVEEDOR_PRODUCTO", indexes = {
        @Index(name = "id_pedido_proveedor_pp", columnList = "id_pedido_proveedor_pp"),
        @Index(name = "id_producto_pp", columnList = "id_producto_pp")
})
public class PedidoProveedorProducto {
    @Id
    @Column(name = "id_pedido_proveedor_producto", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido_proveedor_pp")
    private PedidoProveedor idPedidoProveedorPp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto_pp")
    private Producto idProductoPp;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PedidoProveedor getIdPedidoProveedorPp() {
        return idPedidoProveedorPp;
    }

    public void setIdPedidoProveedorPp(PedidoProveedor idPedidoProveedorPp) {
        this.idPedidoProveedorPp = idPedidoProveedorPp;
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

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

}