package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "PRODUCTO", indexes = {
        @Index(name = "id_proveedor_producto", columnList = "id_proveedor_producto")
})
public class Producto {
    @Id
    @Column(name = "id_producto", nullable = false)
    private Integer id;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    @Column(name = "precio")
    private Float precio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor_producto")
    private Proveedor idProveedorProducto;

    @OneToMany(mappedBy = "idProducto")
    private Set<PedidoCliente> pedidoClientes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idProductoPp")
    private Set<PedidoClienteProducto> pedidoClienteProductos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idProductoPp")
    private Set<PedidoProveedorProducto> pedidoProveedorProductos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idProducto")
    private Set<Stock> stocks = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Proveedor getIdProveedorProducto() {
        return idProveedorProducto;
    }

    public void setIdProveedorProducto(Proveedor idProveedorProducto) {
        this.idProveedorProducto = idProveedorProducto;
    }

    public Set<PedidoCliente> getPedidoClientes() {
        return pedidoClientes;
    }

    public void setPedidoClientes(Set<PedidoCliente> pedidoClientes) {
        this.pedidoClientes = pedidoClientes;
    }

    public Set<PedidoClienteProducto> getPedidoClienteProductos() {
        return pedidoClienteProductos;
    }

    public void setPedidoClienteProductos(Set<PedidoClienteProducto> pedidoClienteProductos) {
        this.pedidoClienteProductos = pedidoClienteProductos;
    }

    public Set<PedidoProveedorProducto> getPedidoProveedorProductos() {
        return pedidoProveedorProductos;
    }

    public void setPedidoProveedorProductos(Set<PedidoProveedorProducto> pedidoProveedorProductos) {
        this.pedidoProveedorProductos = pedidoProveedorProductos;
    }

    public Set<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
    }

}