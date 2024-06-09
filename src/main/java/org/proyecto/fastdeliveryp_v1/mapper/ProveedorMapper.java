package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.proyecto.fastdeliveryp_v1.dto.ProveedorDto;
import org.proyecto.fastdeliveryp_v1.entity.Proveedor;
@Mapper(componentModel = "spring")
public interface ProveedorMapper {
  ProveedorDto toDto(Proveedor proveedor);

  Proveedor toEntity(ProveedorDto proveedorDto);
}
