package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.proyecto.fastdeliveryp_v1.dto.PedidoProveedorProductoInfo;
import org.proyecto.fastdeliveryp_v1.entity.PedidoProveedorProducto;

@Mapper(componentModel = "spring")
public interface PedidoProveedorProductoMapper {
    PedidoProveedorProductoMapper INSTANCE = Mappers.getMapper(PedidoProveedorProductoMapper.class);

    @Mapping(source = "pedidoProveedor.id", target = "idPedidoProveedor")
    @Mapping(source = "producto.id", target = "idProducto")
    @Mapping(source = "producto.nombre", target = "nombreProducto") // Mapea el nombre del producto

    PedidoProveedorProductoInfo toInfo(PedidoProveedorProducto pedidoProveedorProducto);

    @Mapping(source = "idPedidoProveedor", target = "pedidoProveedor.id")
    @Mapping(source = "idProducto", target = "producto.id")

    PedidoProveedorProducto toEntity(PedidoProveedorProductoInfo pedidoProveedorProductoInfo);
}
