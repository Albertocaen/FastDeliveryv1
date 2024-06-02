package org.proyecto.fastdeliveryp_v1.service;

import org.proyecto.fastdeliveryp_v1.entity.*;
import org.proyecto.fastdeliveryp_v1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    public Cliente getClienteById(String dni) {
        return clienteRepository.findById(dni).orElse(null);
    }


    public Cliente getClienteByEmail(String email) {return clienteRepository.findByEmail(email);}

    @Transactional
    public Cliente saveCliente(Cliente cliente) {
        Persona persona = cliente.getPersona();
        if (persona == null) {
            throw new IllegalArgumentException("El cliente debe tener una persona asociada.");
        }
        persona.setDni(cliente.getDniCliente());

        persona.setContraseña(passwordEncoder.encode(persona.getContraseña()));

        // Validar el cliente antes de guardar
        validateCliente(cliente);


        return clienteRepository.save(cliente);
    }


    @Transactional
    public void updateCliente(String dniCliente, Cliente cliente) {
        Persona persona = cliente.getPersona();
        if (persona == null) {
            throw new IllegalArgumentException("El cliente debe tener una persona asociada.");
        }
        persona.setDni(cliente.getDniCliente());

        persona.setContraseña(passwordEncoder.encode(persona.getContraseña()));

        // Validar el cliente antes de actualizar
        validateCliente(cliente);


        clienteRepository.save(cliente);
    }

    @Transactional
    public void deleteCliente(String dniCliente) {

        // El manejo de el delete se facilito debido a el cambio en la base de datos a cascade
        Cliente cliente = clienteRepository.findById(dniCliente).orElse(null);
        if (cliente != null) {
            clienteRepository.delete(cliente);
        }
    }

    //validate
    private void validateCliente(Cliente cliente) {
        if (cliente.getDniCliente() == null || cliente.getDniCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI del cliente no puede estar vacío.");
        }

        Persona persona = cliente.getPersona();
        if (persona == null) {
            throw new IllegalArgumentException("El cliente debe tener una persona asociada.");
        }

        if (persona.getNombre() == null || persona.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la persona no puede estar vacío.");
        }

        if (persona.getEmail() == null || !isValidEmail(persona.getEmail())) {
            throw new IllegalArgumentException("El email de la persona no es válido.");
        }

        // Verificar si el email ya existe en la base de datos
        Persona existingPersona = personaRepository.findByEmail(persona.getEmail());
        if (existingPersona != null && !existingPersona.getDni().equals(persona.getDni())) {
            throw new IllegalArgumentException("El email ya está en uso.");
        }

        if (persona.getContraseña() == null || persona.getContraseña().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

}


