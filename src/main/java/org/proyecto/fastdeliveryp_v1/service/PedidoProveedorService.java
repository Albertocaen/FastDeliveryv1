package org.proyecto.fastdeliveryp_v1.service;

import java.lang.Long;
import java.util.List;
import java.util.stream.Collectors;

import org.proyecto.fastdeliveryp_v1.dto.PedidoProveedorDto;
import org.proyecto.fastdeliveryp_v1.mapper.PedidoProveedorMapper;
import org.proyecto.fastdeliveryp_v1.repository.PedidoProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoProveedorService {
  @Autowired
  private PedidoProveedorRepository repository;

  @Autowired
  private PedidoProveedorMapper mapper;

  public List<PedidoProveedorDto> findAll() {
    return repository.findAll()
            .stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
  }

  public PedidoProveedorDto findById(Integer id) {
    return repository.findById(id).map(mapper::toDto).orElse(null);
  }

  public PedidoProveedorDto save(PedidoProveedorDto dto) {
    return mapper.toDto(repository.save(mapper.toEntity(dto)));
  }

  public void deleteById(Integer id) {
    repository.deleteById(id);
  }
}
