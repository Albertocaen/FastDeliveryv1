package org.proyecto.fastdeliveryp_v1.service;

import org.proyecto.fastdeliveryp_v1.entity.Cliente;
import org.proyecto.fastdeliveryp_v1.entity.Persona;
import org.proyecto.fastdeliveryp_v1.repository.ClienteRepository;
import org.proyecto.fastdeliveryp_v1.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonaService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public void registerUser(String dni, String email, String nombre, String apellido, String telefono, String contrasena) {
        // Verificar si el email ya existe en la base de datos
        if (personaRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("El email ya está en uso.");
        }

        // Crear una nueva instancia de Persona
        Persona persona = new Persona();
        persona.setDni(dni);
        persona.setEmail(email);
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setTelefono(telefono);
        persona.setContraseña(passwordEncoder.encode(contrasena));
        persona.setUsuario("cliente"); // Establecer el rol del usuario como "cliente"

        // Guardar la persona
        personaRepository.save(persona);

        // Crear y guardar el cliente asociado
        Cliente cliente = new Cliente();
        cliente.setPersona(persona);
        cliente.setDniCliente(dni);
        clienteRepository.save(cliente);
    }
}