package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "PERSONA")
public class Persona {
    @Id
    @Column(name = "DNI", nullable = false, length = 20)
    private String dni;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @Column(name = "apellido", length = 50)
    private String apellido;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "contrasena", length = 50)
    private String contrasena;

    @Column(name = "usuario", length = 50)
    private String usuario;

    @OneToOne(mappedBy = "dniAdmin")
    private Admin admin;

    @OneToOne(mappedBy = "dniCliente")
    private Cliente cliente;

    @OneToOne(mappedBy = "dniRepartidor")
    private Repartidor repartidor;

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

}