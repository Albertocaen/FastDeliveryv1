package org.proyecto.fastdeliveryp_v1.service;

import org.proyecto.fastdeliveryp_v1.entity.Repartidor;
import org.proyecto.fastdeliveryp_v1.repository.RepartidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepartidorService {

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
        List<Repartidor> repartidores = repartidorRepository.findByEstadoDeDisponibilidadAndCantidadPedidosLessThanEqual("Disponible", 3);
        if (!repartidores.isEmpty()) {
            Repartidor repartidor = repartidores.get(0); // Asigna el primer repartidor disponible
            repartidor.setCantidadPedidos(repartidor.getCantidadPedidos() + 1);
            repartidorRepository.save(repartidor);
            return repartidor;
        } else {
            throw new RuntimeException("No hay repartidores disponibles en este momento");
        }
    }
}
