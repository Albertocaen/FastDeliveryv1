package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "ADMIN")
public class Admin {
    @Id
    @Column(name = "DNI_ADMIN", nullable = false, length = 20)
    private String dniAdmin;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DNI_ADMIN", nullable = false)
    private Persona persona;

    @OneToMany(mappedBy = "dniAdminPedido")
    private Set<PedidoProveedor> pedidoProveedors = new LinkedHashSet<>();

    @OneToMany(mappedBy = "dniAdmin")
    private Set<Stock> stocks = new LinkedHashSet<>();

    public String getDniAdmin() {
        return dniAdmin;
    }

    public void setDniAdmin(String dniAdmin) {
        this.dniAdmin = dniAdmin;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Set<PedidoProveedor> getPedidoProveedors() {
        return pedidoProveedors;
    }

    public void setPedidoProveedors(Set<PedidoProveedor> pedidoProveedors) {
        this.pedidoProveedors = pedidoProveedors;
    }

    public Set<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
    }

}