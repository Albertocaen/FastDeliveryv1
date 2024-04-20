package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "CLIENTE")
public class Cliente {
    @Id
    @Column(name = "DNI_CLIENTE", nullable = false, length = 20)
    private String dniCliente;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DNI_CLIENTE", nullable = false)
    private Persona persona;

    @Column(name = "direccion", length = 100)
    private String direccion;

    @Column(name = "ciudad", length = 50)
    private String ciudad;

    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_pedido")
    private Repartidor clientePedido;

    @OneToMany(mappedBy = "dniClientePedido")
    private Set<PedidoCliente> pedidoClientes = new LinkedHashSet<>();

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Repartidor getClientePedido() {
        return clientePedido;
    }

    public void setClientePedido(Repartidor clientePedido) {
        this.clientePedido = clientePedido;
    }

    public Set<PedidoCliente> getPedidoClientes() {
        return pedidoClientes;
    }

    public void setPedidoClientes(Set<PedidoCliente> pedidoClientes) {
        this.pedidoClientes = pedidoClientes;
    }

}