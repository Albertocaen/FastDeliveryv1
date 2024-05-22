package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;
@Data
@Entity
@Table(name = "REPARTIDOR")
public class Repartidor {
    @Id
    @Column(name = "DNI_REPARTIDOR", nullable = false, length = 20)
    private String dniRepartidor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placa_vehiculo")
    private Vehiculo placaVehiculo;

    @OneToMany(mappedBy = "dniRepartidorPedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoCliente> pedidos;

}