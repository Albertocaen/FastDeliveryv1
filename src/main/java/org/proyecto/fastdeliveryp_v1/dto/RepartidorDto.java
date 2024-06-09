package org.proyecto.fastdeliveryp_v1.dto;

import java.lang.Float;
import java.lang.Integer;
import java.lang.String;
import java.util.List;
import lombok.Data;
import org.proyecto.fastdeliveryp_v1.entity.Persona;
import org.proyecto.fastdeliveryp_v1.entity.Vehiculo;

@Data
public class RepartidorDto {
  private String dniRepartidor;
  private PersonaDto persona;
  private Float calificacion;
  private String horarioTrabajo;
  private String estadoDeDisponibilidad;
  private Integer cantidadPedidos;
  private VehiculoDto vehiculo;
}
