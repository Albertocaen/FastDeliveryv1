package org.proyecto.fastdeliveryp_v1.service;

import java.lang.Long;
import java.util.List;
import org.proyecto.fastdeliveryp_v1.dto.ProveedorDto;
import org.proyecto.fastdeliveryp_v1.mapper.ProveedorMapper;
import org.proyecto.fastdeliveryp_v1.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProveedorService {
  @Autowired
  private ProveedorRepository repository;
 @Autowired
  private ProveedorMapper mapper;

  public List<ProveedorDto> findAll() {
    return repository.findAll().stream().map(mapper::toDto).collect(java.util.stream.Collectors.toList());
  }

  public ProveedorDto findById(Integer id) {
    return repository.findById(id).map(mapper::toDto).orElse(null);
  }

  public ProveedorDto save(ProveedorDto dto) {
    return mapper.toDto(repository.save(mapper.toEntity(dto)));
  }

  public void deleteById(Integer  id) {
    repository.deleteById(id);
  }
}
