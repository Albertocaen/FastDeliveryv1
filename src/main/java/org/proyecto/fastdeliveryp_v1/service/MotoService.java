package org.proyecto.fastdeliveryp_v1.service;

import java.lang.Long;
import java.util.List;
import org.proyecto.fastdeliveryp_v1.dto.MotoDto;
import org.proyecto.fastdeliveryp_v1.mapper.MotoMapper;
import org.proyecto.fastdeliveryp_v1.repository.MotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MotoService {
  @Autowired
  private MotoRepository repository;
@Autowired
  private MotoMapper mapper;

  public List<MotoDto> findAll() {
    return repository.findAll().stream().map(mapper::toDto).collect(java.util.stream.Collectors.toList());
  }

  public MotoDto findById(Long id) {
    return repository.findById(id).map(mapper::toDto).orElse(null);
  }

  public MotoDto save(MotoDto dto) {
    return mapper.toDto(repository.save(mapper.toEntity(dto)));
  }

  public void deleteById(Long id) {
    repository.deleteById(id);
  }
}
