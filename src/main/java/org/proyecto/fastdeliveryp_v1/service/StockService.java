package org.proyecto.fastdeliveryp_v1.service;

import java.lang.Long;
import java.util.List;
import org.proyecto.fastdeliveryp_v1.dto.StockDto;
import org.proyecto.fastdeliveryp_v1.mapper.StockMapper;
import org.proyecto.fastdeliveryp_v1.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {
  private StockRepository repository;

  private StockMapper mapper;

  public List<StockDto> findAll() {
    return repository.findAll().stream().map(mapper::toDto).collect(java.util.stream.Collectors.toList());
  }

  public StockDto findById(String id) {
    return repository.findById(id).map(mapper::toDto).orElse(null);
  }

  public StockDto save(StockDto dto) {
    return mapper.toDto(repository.save(mapper.toEntity(dto)));
  }

  public void deleteById(String id) {
    repository.deleteById(id);
  }
}
