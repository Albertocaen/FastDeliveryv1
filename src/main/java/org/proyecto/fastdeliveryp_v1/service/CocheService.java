package org.proyecto.fastdeliveryp_v1.service;

import java.lang.Long;
import java.util.List;
import org.proyecto.fastdeliveryp_v1.dto.CocheDto;
import org.proyecto.fastdeliveryp_v1.mapper.CocheMapper;
import org.proyecto.fastdeliveryp_v1.repository.CocheRepository;
import org.springframework.stereotype.Service;

@Service
public class CocheService {
  private CocheRepository repository;

  private CocheMapper mapper;

  public List<CocheDto> findAll() {
    return repository.findAll().stream().map(mapper::toDto).collect(java.util.stream.Collectors.toList());
  }

  public CocheDto findById(Long id) {
    return repository.findById(id).map(mapper::toDto).orElse(null);
  }

  public CocheDto save(CocheDto dto) {
    return mapper.toDto(repository.save(mapper.toEntity(dto)));
  }

  public void deleteById(Long id) {
    repository.deleteById(id);
  }
}
