package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "REPARTIDOR")
public class Repartidor {
    /**
     * DNI del repartidor, que actúa como clave primaria.
     */
    @Id
    @Column(name = "DNI_REPARTIDOR", nullable = false, length = 20)
    private String dniRepartidor;

    /**
     * Relación uno a uno con la entidad Persona.
     * Representa la persona asociada al repartidor.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "DNI_REPARTIDOR", nullable = false)
    private Persona persona;

    @Column(name = "calificacion")
    private Float calificacion;

    @Column(name = "horario_trabajo", length = 100)
    private String horarioTrabajo;

    @Column(name = "estado_de_disponibilidad", length = 50)
    private String estadoDeDisponibilidad;

    @Column(name = "cantidad_pedidos")
    private Integer cantidadPedidos;

    /**
     * Relación muchos a uno con la entidad Vehiculo.
     * Representa el vehículo asignado al repartidor.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "placa_vehiculo")
    private Vehiculo placaVehiculo;

    /**
     * Relación uno a muchos con la entidad PedidoCliente.
     * Representa los pedidos realizados por el repartidor.
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "dniRepartidorPedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoCliente> pedidos;

}