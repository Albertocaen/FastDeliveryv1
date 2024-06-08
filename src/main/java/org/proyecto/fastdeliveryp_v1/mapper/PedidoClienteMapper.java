package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.proyecto.fastdeliveryp_v1.dto.PedidoClienteDto;
import org.proyecto.fastdeliveryp_v1.entity.PedidoCliente;
@Mapper(componentModel = "spring")
public interface PedidoClienteMapper {
  PedidoClienteDto toDto(PedidoCliente entity);

  PedidoCliente toEntity(PedidoClienteDto dto);
}
