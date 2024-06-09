package org.proyecto.fastdeliveryp_v1.service;

import java.lang.Long;
import java.util.List;
import org.proyecto.fastdeliveryp_v1.dto.VehiculoDto;
import org.proyecto.fastdeliveryp_v1.mapper.VehiculoMapper;
import org.proyecto.fastdeliveryp_v1.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehiculoService {
  @Autowired
  private VehiculoRepository repository;
  @Autowired
  private VehiculoMapper mapper;

  public List<VehiculoDto> findAll() {
    return repository.findAll().stream().map(mapper::toDto).collect(java.util.stream.Collectors.toList());
  }

  public VehiculoDto findById(Long id) {
    return repository.findById(id).map(mapper::toDto).orElse(null);
  }

  public VehiculoDto save(VehiculoDto dto) {
    return mapper.toDto(repository.save(mapper.toEntity(dto)));
  }

  public void deleteById(Long id) {
    repository.deleteById(id);
  }
}
