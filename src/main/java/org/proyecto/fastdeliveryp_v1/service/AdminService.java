package org.proyecto.fastdeliveryp_v1.service;

import java.lang.Long;
import java.util.List;
import org.proyecto.fastdeliveryp_v1.dto.AdminDto;
import org.proyecto.fastdeliveryp_v1.mapper.AdminMapper;
import org.proyecto.fastdeliveryp_v1.repository.AdminRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
  private AdminRepository repository;

  private AdminMapper mapper;

  public List<AdminDto> findAll() {
    return repository.findAll().stream().map(mapper::toDto).collect(java.util.stream.Collectors.toList());
  }

  public AdminDto findById(String id) {
    return repository.findById(id).map(mapper::toDto).orElse(null);
  }

  public AdminDto save(AdminDto dto) {
    return mapper.toDto(repository.save(mapper.toEntity(dto)));
  }

  public void deleteById(String id) {
    repository.deleteById(id);
  }
}
