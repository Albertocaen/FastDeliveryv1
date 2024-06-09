package org.proyecto.fastdeliveryp_v1.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PedidoProveedorProductoInfo {
    private Integer id;
    private Integer idPedidoProveedor;
    private Integer idProducto;
    private Integer cantidad;
    private float precio;
    private LocalDateTime fecha;
    private String nombreProducto;
    private String comentarios;
}
