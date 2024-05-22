package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
@Data
@Entity
@Table(name = "VEHICULO")
public class Vehiculo {
    @Id
    @Column(name = "placa_vehiculo", nullable = false, length = 20)
    private String placaVehiculo;

    @Column(name = "marca", length = 50)
    private String marca;

    @Column(name = "modelo", length = 50)
    private String modelo;

    @Column(name = "color", length = 50)
    private String color;

    @OneToMany(mappedBy = "placaVehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Repartidor> repartidores;
}