package org.proyecto.fastdeliveryp_v1.service;

import org.proyecto.fastdeliveryp_v1.dto.RepartidorDto;
import org.proyecto.fastdeliveryp_v1.entity.PedidoCliente;
import org.proyecto.fastdeliveryp_v1.entity.Persona;
import org.proyecto.fastdeliveryp_v1.entity.Repartidor;
import org.proyecto.fastdeliveryp_v1.mapper.RepartidorMapper;
import org.proyecto.fastdeliveryp_v1.repository.RepartidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RepartidorService {

    @Autowired
    private PedidoClienteService pedidoClienteService;
    @Autowired
    private RepartidorRepository repartidorRepository;
    @Autowired
    RepartidorMapper mapper;

    public List<Repartidor> getAllRepartidores() {
        return repartidorRepository.findAll();
    }

    public Optional<Repartidor> getRepartidorById(String dni) {
        return repartidorRepository.findById(dni);
    }

    public void saveRepartidor(Repartidor repartidor) {
        repartidorRepository.save(repartidor);
    }

    public Repartidor asignarRepartidorDisponible() {
        List<Repartidor> repartidores = repartidorRepository.findAll();

        for (Repartidor repartidor : repartidores) {
            liberarPedidosEntregados(repartidor);
            if (repartidor.getCantidadPedidos() < 3) {
                repartidor.setCantidadPedidos(repartidor.getCantidadPedidos() + 1);
                return repartidorRepository.save(repartidor);
            }
        }
        return null;
    }

    private void liberarPedidosEntregados(Repartidor repartidor) {
        List<PedidoCliente> pedidos = pedidoClienteService.getPedidosByRepartidor(repartidor);
        long pedidosNoEntregados = pedidos.stream().filter(p -> !"Entregado".equalsIgnoreCase(p.getEstado())).count();
        repartidor.setCantidadPedidos((int) pedidosNoEntregados);
        repartidorRepository.save(repartidor);
    }


    public List<RepartidorDto> findAll() {
        List<Repartidor> repartidores = repartidorRepository.findAll();
        return repartidores.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    public RepartidorDto findById(String id) {
        return repartidorRepository.findById(id).map(mapper::toDto).orElse(null);
    }

    @PostMapping
    public RepartidorDto save(RepartidorDto dto) {
        Repartidor repartidor = mapper.toEntity(dto);
       Repartidor savedRepartidor  = repartidorRepository.save(repartidor);
        return mapper.toDto(savedRepartidor);
    }

    public void deleteById(String id) {
        repartidorRepository.deleteById(id);
    }
}

