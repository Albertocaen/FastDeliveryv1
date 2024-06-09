package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.proyecto.fastdeliveryp_v1.dto.PedidoClienteDto;
import org.proyecto.fastdeliveryp_v1.entity.PedidoCliente;
@Mapper(componentModel = "spring")
public interface PedidoClienteMapper {
  @Mapping(source = "dniClientePedido", target = "cliente")
  @Mapping(source = "dniRepartidorPedido", target = "repartidor")
  PedidoClienteDto toDto(PedidoCliente entity);

  @Mapping(source = "cliente", target = "dniClientePedido")
  @Mapping(source = "repartidor", target = "dniRepartidorPedido")
  PedidoCliente toEntity(PedidoClienteDto dto);
}
