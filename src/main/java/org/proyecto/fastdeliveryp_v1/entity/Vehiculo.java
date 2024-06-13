package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Entidad que representa un vehículo en la base de datos.
 */
@Data
@Entity
@Table(name = "VEHICULO")
//la estrategia JOINED crea una tabla separada para cada clase en la jerarquía
@Inheritance(strategy = InheritanceType.JOINED)
public class Vehiculo {
    /**
     * Placa del vehículo, que actúa como clave primaria.
     */
    @Id
    @Column(name = "placa_vehiculo", nullable = false, length = 20)
    private String placaVehiculo;

    @Column(name = "marca", length = 50)
    private String marca;

    @Column(name = "modelo", length = 50)
    private String modelo;

    @Column(name = "color", length = 50)
    private String color;

    /**
     * Relación uno a muchos con la entidad Repartidor.
     * Representa los repartidores asignados a este vehículo.
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "placaVehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Repartidor> repartidores;
}