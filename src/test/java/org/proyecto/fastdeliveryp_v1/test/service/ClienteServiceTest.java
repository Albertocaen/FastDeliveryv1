package org.proyecto.fastdeliveryp_v1.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.proyecto.fastdeliveryp_v1.entity.Cliente;
import org.proyecto.fastdeliveryp_v1.entity.Persona;
import org.proyecto.fastdeliveryp_v1.repository.ClienteRepository;
import org.proyecto.fastdeliveryp_v1.repository.PersonaRepository;
import org.proyecto.fastdeliveryp_v1.service.ClienteService;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClienteService clienteService;



    private Cliente cliente1;;
    private Persona persona1;

    @BeforeEach
    void setUp() {
        persona1 = new Persona();
        persona1.setDni("123456789");
        persona1.setNombre("Pedro");
        persona1.setApellido("Pedro");
        persona1.setEmail("testemail@test.com");
        persona1.setContraseña("123456");
        persona1.setCliente(cliente1);

        cliente1 = new Cliente();
        cliente1.setDniCliente("123456789");
        cliente1.setPersona(persona1);
        cliente1.setDireccion("Calle");
    }
    @Test
    void testClienteGetAll(){
        when(clienteRepository.findAll()).thenReturn(Collections.singletonList(cliente1));
        List<Cliente> clientes = clienteService.getAllClientes();
        assertEquals(1, clientes.size());
        verify(clienteRepository, times(1)).findAll();
    };

    @Test
    void testClienteGetById(){
        when( clienteRepository.findById(cliente1.getDniCliente())).thenReturn(Optional.of(cliente1));
        Cliente cliente = clienteService.getClienteById(cliente1.getDniCliente());
        assertEquals(cliente1, cliente);
        verify(clienteRepository, times(1)).findById(cliente1.getDniCliente());
    }

    @Test
    void testClienteGetByEmail(){
        when(personaRepository.findByEmail(persona1.getEmail())).thenReturn(persona1);
        when(clienteRepository.findById(persona1.getDni())).thenReturn(Optional.of(cliente1));
        Cliente cliente = clienteService.getClienteByEmail(persona1.getEmail());
        assertNotNull(cliente);
        assertEquals(cliente1.getDniCliente(), cliente.getDniCliente());
        verify(clienteRepository, times(1)).findById(persona1.getDni());
        verify(personaRepository, times(1)).findByEmail(persona1.getEmail());
    }

    @Test
    void shouldSaveValidCliente(){

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPass");

        when(personaRepository.findByEmail(persona1.getEmail()))
                .thenReturn(null); // para que validate no diga email duplicado

        when(clienteRepository.save(cliente1))
                .thenReturn(cliente1);

        Cliente saved =
                clienteService.saveCliente(cliente1);

        assertNotNull(saved);

        assertEquals(
                "encodedPass",
                saved.getPersona().getContraseña()
        );

        verify(passwordEncoder)
                .encode("123456");

        verify(clienteRepository)
                .save(cliente1);
    }

    @Test
    void shouldThrowWhenPersonaIsNull(){

        Cliente cliente = new Cliente();

        assertThrows(
                IllegalArgumentException.class,
                () -> clienteService.saveCliente(cliente)
        );

    }
}
