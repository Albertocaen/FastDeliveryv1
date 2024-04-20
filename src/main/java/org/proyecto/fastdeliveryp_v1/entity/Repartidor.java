package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "REPARTIDOR")
public class Repartidor {
    @Id
    @Column(name = "DNI_REPARTIDOR", nullable = false, length = 20)
    private String dniRepartidor;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
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

    @OneToMany(mappedBy = "clientePedido")
    private Set<Cliente> clientes = new LinkedHashSet<>();

    public String getDniRepartidor() {
        return dniRepartidor;
    }

    public void setDniRepartidor(String dniRepartidor) {
        this.dniRepartidor = dniRepartidor;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Float calificacion) {
        this.calificacion = calificacion;
    }

    public String getHorarioTrabajo() {
        return horarioTrabajo;
    }

    public void setHorarioTrabajo(String horarioTrabajo) {
        this.horarioTrabajo = horarioTrabajo;
    }

    public String getEstadoDeDisponibilidad() {
        return estadoDeDisponibilidad;
    }

    public void setEstadoDeDisponibilidad(String estadoDeDisponibilidad) {
        this.estadoDeDisponibilidad = estadoDeDisponibilidad;
    }

    public Integer getCantidadPedidos() {
        return cantidadPedidos;
    }

    public void setCantidadPedidos(Integer cantidadPedidos) {
        this.cantidadPedidos = cantidadPedidos;
    }

    public Vehiculo getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(Vehiculo placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }

    public Set<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(Set<Cliente> clientes) {
        this.clientes = clientes;
    }

}