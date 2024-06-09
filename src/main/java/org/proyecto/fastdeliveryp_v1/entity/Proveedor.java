package org.proyecto.fastdeliveryp_v1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "PROVEEDOR")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor", nullable = false)
    private Integer id;

    @Column(name = "nombre_empresa", length = 50)
    private String nombreEmpresa;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", length = 50)
    private String email;

    @OneToMany(mappedBy = "idProveedorPedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<PedidoProveedor> pedidos;

}