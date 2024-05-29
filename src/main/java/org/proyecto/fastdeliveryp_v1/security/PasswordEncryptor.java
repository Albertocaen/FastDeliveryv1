package org.proyecto.fastdeliveryp_v1.security;
import org.proyecto.fastdeliveryp_v1.entity.Persona;
import org.proyecto.fastdeliveryp_v1.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasswordEncryptor implements CommandLineRunner {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

  //Este script se ejecutará al iniciar la aplicación y cifrará todas las contraseñas que no estén cifradas.
    @Override
    public void run(String... args) throws Exception {
        // Obtiene todos los usuarios
        List<Persona> personas= personaRepository.findAll();

        for (Persona persona : personas) {
            // Cifra la contraseña si no está ya cifrada
            if (!persona.getContraseña().startsWith("$2a$")) {
                persona.setContraseña(passwordEncoder.encode(persona.getContraseña()));
                personaRepository.save(persona);
            }
        }
    }
}