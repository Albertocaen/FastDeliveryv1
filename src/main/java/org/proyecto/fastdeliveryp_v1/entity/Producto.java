package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.proyecto.fastdeliveryp_v1.interfacE.ImageUrl;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un producto en la base de datos.
 */
@Data
@Entity
@Table(name = "PRODUCTO")
public class Producto {
    /**
     * ID del producto, que actúa como clave primaria.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto", nullable = false)
    private Integer id;

    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    @Column(name = "nombre", length = 50)
    private String nombre;

    @Size(max = 1000, message = "La descripción no puede tener más de 1000 caracteres")
    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
    @Column(name = "precio")
    private Float precio;

    @ImageUrl
    @Column(name = "img")
    private String img;

    /**
     * Relación muchos a uno con la entidad Proveedor.
     * Representa el proveedor asociado al producto.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor_producto")
    private Proveedor idProveedorProducto;

    /**
     * Relación uno a muchos con la entidad PedidoClienteProducto.
     * Representa los pedidos de clientes asociados al producto.
     */
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PedidoClienteProducto> pedidosClientes = new HashSet<>();

    /**
     * Relación uno a muchos con la entidad PedidoProveedorProducto.
     * Representa los pedidos a proveedores asociados al producto.
     */
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PedidoProveedorProducto> pedidosProveedores = new HashSet<>();

}