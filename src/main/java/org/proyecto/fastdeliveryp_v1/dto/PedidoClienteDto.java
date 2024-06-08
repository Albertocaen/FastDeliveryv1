package org.proyecto.fastdeliveryp_v1.dto;

import java.lang.Float;
import java.lang.Integer;
import java.lang.String;
import java.time.LocalDate;
import lombok.Data;
import org.proyecto.fastdeliveryp_v1.entity.Cliente;
import org.proyecto.fastdeliveryp_v1.entity.Producto;
import org.proyecto.fastdeliveryp_v1.entity.Repartidor;

@Data
public class PedidoClienteDto {
  private Integer id;

  private Integer cantidad;

  private LocalDate fechaPedido;

  private Float peso;

  private String estado;

  private Cliente dniClientePedido;

  private Repartidor dniRepartidorPedido;

  private Producto producto;
}
