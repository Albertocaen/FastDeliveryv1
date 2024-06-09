package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.proyecto.fastdeliveryp_v1.dto.PedidoProveedorDto;
import org.proyecto.fastdeliveryp_v1.entity.PedidoProveedor;

@Mapper(componentModel = "spring", uses = {ProveedorMapper.class, AdminMapper.class})
public interface PedidoProveedorMapper {
  @Mapping(source = "idProveedorPedido", target = "idProveedorPedido")
  @Mapping(source = "dniAdminPedido", target = "dniAdminPedido")
  PedidoProveedorDto toDto(PedidoProveedor pedidoProveedor);

  @Mapping(source = "idProveedorPedido", target = "idProveedorPedido")
  @Mapping(source = "dniAdminPedido", target = "dniAdminPedido")
  PedidoProveedor toEntity(PedidoProveedorDto pedidoProveedorDto);
}
