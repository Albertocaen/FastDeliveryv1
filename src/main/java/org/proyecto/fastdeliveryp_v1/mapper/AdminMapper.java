package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.proyecto.fastdeliveryp_v1.dto.AdminDto;
import org.proyecto.fastdeliveryp_v1.entity.Admin;
@Mapper(componentModel = "spring")
public interface AdminMapper {
  AdminDto toDto(Admin entity);

  Admin toEntity(AdminDto dto);
}
