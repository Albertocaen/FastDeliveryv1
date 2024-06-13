package org.proyecto.fastdeliveryp_v1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

/**
 * Entidad que representa a un administrador en la base de datos.
 */
@Data
@Entity
@Getter
@Setter
@Table(name = "ADMIN")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Admin {
    /**
     * DNI del administrador, que actúa como clave primaria.
     */
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "DNI_ADMIN", nullable = false, length = 20)
    private String dniAdmin;

    /**
     * Relación uno a uno con la entidad Persona.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "DNI_ADMIN", nullable = false)
    @JsonBackReference
    private Persona persona;

    /**
     * Relación uno a muchos con la entidad Stock.
     */
    @OneToMany(mappedBy = "dniAdmin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stock> stocks;

    /**
     * Relación uno a muchos con la entidad PedidoProveedor.
     */
    @OneToMany(mappedBy = "dniAdminPedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoProveedor> pedidosProveedor;


}