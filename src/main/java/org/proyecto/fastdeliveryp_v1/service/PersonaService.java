package org.proyecto.fastdeliveryp_v1.service;

import org.proyecto.fastdeliveryp_v1.dto.PersonaDto;
import org.proyecto.fastdeliveryp_v1.entity.Cliente;
import org.proyecto.fastdeliveryp_v1.entity.Persona;
import org.proyecto.fastdeliveryp_v1.mapper.PersonaMapper;
import org.proyecto.fastdeliveryp_v1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
public class PersonaService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    PersonaMapper mapper;


    public Persona getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return personaRepository.findByEmail(email);
    }

    @Transactional
    public void updatePersona(Persona persona) {
        Persona existingPersona = personaRepository.findById(persona.getDni()).orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        existingPersona.setNombre(persona.getNombre());
        existingPersona.setApellido(persona.getApellido());
        existingPersona.setTelefono(persona.getTelefono());

        if (persona.getCliente() != null) {
            if (existingPersona.getCliente() == null) {
                existingPersona.setCliente(persona.getCliente());
            } else {
                existingPersona.getCliente().setDireccion(persona.getCliente().getDireccion());
                existingPersona.getCliente().setCiudad(persona.getCliente().getCiudad());
                existingPersona.getCliente().setCodigoPostal(persona.getCliente().getCodigoPostal());
            }
        }

        if (persona.getRepartidor() != null) {
            if (existingPersona.getRepartidor() == null) {
                existingPersona.setRepartidor(persona.getRepartidor());
            } else {
                existingPersona.getRepartidor().setCalificacion(persona.getRepartidor().getCalificacion());
                existingPersona.getRepartidor().setHorarioTrabajo(persona.getRepartidor().getHorarioTrabajo());
                existingPersona.getRepartidor().setEstadoDeDisponibilidad(persona.getRepartidor().getEstadoDeDisponibilidad());
            }
        }

        personaRepository.save(existingPersona);
    }


    public void registerUser(String dni, String email, String nombre, String apellido, String telefono, String contrasena) {
        // Verificar si el email ya existe en la base de datos
        if (personaRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("El email ya está en uso.");
        }

        Persona persona = new Persona();
        persona.setDni(dni);
        persona.setEmail(email);
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setTelefono(telefono);
        persona.setContraseña(passwordEncoder.encode(contrasena));
        persona.setUsuario("cliente");

        // Guardar la persona
        personaRepository.save(persona);

        // Crear y guardar el cliente asociado
        Cliente cliente = new Cliente();
        cliente.setPersona(persona);
        cliente.setDniCliente(dni);
        clienteRepository.save(cliente);
    }

    public List<PersonaDto> findAll() {
        return personaRepository.findAll().stream().map(mapper::toDto).collect(java.util.stream.Collectors.toList());
    }
    public PersonaDto findById(String id) {
        return personaRepository.findById(id).map(mapper::toDto).orElse(null);
    }

    @PostMapping
    public PersonaDto save(PersonaDto dto) {
        Persona persona = mapper.toEntity(dto);
        Persona savedPersona  = personaRepository.save(persona);
        return mapper.toDto(savedPersona);
    }

    public void deleteById(String id) {
        clienteRepository.deleteById(id);
    }
}