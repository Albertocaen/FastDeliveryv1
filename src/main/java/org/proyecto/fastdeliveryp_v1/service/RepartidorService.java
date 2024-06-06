package org.proyecto.fastdeliveryp_v1.service;

import org.proyecto.fastdeliveryp_v1.entity.PedidoCliente;
import org.proyecto.fastdeliveryp_v1.entity.Repartidor;
import org.proyecto.fastdeliveryp_v1.repository.RepartidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepartidorService {

    @Autowired
    private PedidoClienteService pedidoClienteService;
    @Autowired
    private RepartidorRepository repartidorRepository;

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
}

