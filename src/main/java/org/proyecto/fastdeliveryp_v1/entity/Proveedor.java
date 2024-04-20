package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "PROVEEDOR")
public class Proveedor {
    @Id
    @Column(name = "id_proveedor", nullable = false)
    private Integer id;

    @Column(name = "nombre_empresa", length = 50)
    private String nombreEmpresa;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", length = 50)
    private String email;

    @OneToMany(mappedBy = "idProveedorPedido")
    private Set<PedidoProveedor> pedidoProveedors = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idProveedorProducto")
    private Set<Producto> productos = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<PedidoProveedor> getPedidoProveedors() {
        return pedidoProveedors;
    }

    public void setPedidoProveedors(Set<PedidoProveedor> pedidoProveedors) {
        this.pedidoProveedors = pedidoProveedors;
    }

    public Set<Producto> getProductos() {
        return productos;
    }

    public void setProductos(Set<Producto> productos) {
        this.productos = productos;
    }

}