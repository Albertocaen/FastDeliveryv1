package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.proyecto.fastdeliveryp_v1.dto.AdminDto;
import org.proyecto.fastdeliveryp_v1.dto.PedidoProveedorDto;
import org.proyecto.fastdeliveryp_v1.dto.ProveedorDto;
import org.proyecto.fastdeliveryp_v1.entity.Admin;
import org.proyecto.fastdeliveryp_v1.entity.PedidoProveedor;
import org.proyecto.fastdeliveryp_v1.entity.Persona;
import org.proyecto.fastdeliveryp_v1.entity.Proveedor;

@Mapper(componentModel = "spring", uses = {PedidoProveedorProductoMapper.class})
public interface PedidoProveedorMapper {
  PedidoProveedorMapper INSTANCE = Mappers.getMapper(PedidoProveedorMapper.class);

  @Mapping(source = "idProveedorPedido", target = "idProveedorPedido")
  @Mapping(source = "dniAdminPedido", target = "dniAdminPedido")
  PedidoProveedorDto toDto(PedidoProveedor pedidoProveedor);

  @Mapping(source = "idProveedorPedido", target = "idProveedorPedido.id")
  @Mapping(source = "dniAdminPedido", target = "dniAdminPedido.dniAdmin")
  PedidoProveedor toEntity(PedidoProveedorDto pedidoProveedorDto);

  // Method to map ProveedorDto to Integer
  default Integer map(ProveedorDto value) {
    return value != null ? value.getId() : null;
  }

  // Method to map Integer to Proveedor
  default Proveedor map(Integer value) {
    Proveedor proveedor = new Proveedor();
    proveedor.setId(value);
    return proveedor;
  }

  // Method to map AdminDto to String
  default String map(AdminDto value) {
    return value != null ? value.getDniAdmin() : null;
  }

  // Method to map String to Admin
  default Admin map(String value) {
    Admin admin = new Admin();
    admin.setDniAdmin(value);
    return admin;
  }

  // Method to map Admin to AdminDto
  default AdminDto map(Admin value) {
    if (value == null) return null;
    AdminDto dto = new AdminDto();
    dto.setDniAdmin(value.getDniAdmin());
    dto.setNombre(value.getPersona().getNombre());
    dto.setApellido(value.getPersona().getApellido());
    return dto;
  }

  // Method to map Proveedor to ProveedorDto
  default ProveedorDto map(Proveedor value) {
    if (value == null) return null;
    ProveedorDto dto = new ProveedorDto();
    dto.setId(value.getId());
    dto.setNombreEmpresa(value.getNombreEmpresa());
    return dto;
  }
}
