package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.proyecto.fastdeliveryp_v1.dto.PedidoProveedorDto;
import org.proyecto.fastdeliveryp_v1.entity.PedidoProveedor;
@Mapper(componentModel = "spring")
public interface PedidoProveedorMapper {
  PedidoProveedorDto toDto(PedidoProveedor entity);

  PedidoProveedor toEntity(PedidoProveedorDto dto);
}
