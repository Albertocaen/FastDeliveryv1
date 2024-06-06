package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "PROVEEDOR")
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

}