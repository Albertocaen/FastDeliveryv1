package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


import java.util.List;

@Entity
@Table(name = "CLIENTE")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cliente {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "DNI_CLIENTE", nullable = false, length = 20)
    private String dniCliente;

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

    @ToString.Exclude
    @OneToMany(mappedBy = "dniClientePedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoCliente> pedidos;
}