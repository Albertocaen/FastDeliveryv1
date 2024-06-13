package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


import java.util.List;

/**
 * Entidad que representa a un cliente en la base de datos.
 */
@Entity
@Table(name = "CLIENTE")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cliente {
    /**
     * DNI del cliente, que actúa como clave primaria.
     */
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "DNI_CLIENTE", nullable = false, length = 20)
    private String dniCliente;

    /**
     * Relación uno a uno con la entidad Persona.
     */
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapsId
    @ToString.Exclude
    @JoinColumn(name = "DNI_CLIENTE")
    private Persona persona;


    @Column(name = "direccion", length = 100)
    private String direccion;


    @Column(name = "ciudad", length = 50)
    private String ciudad;

    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    /**
     * Relación uno a muchos con la entidad PedidoCliente.
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "dniClientePedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoCliente> pedidos;
}