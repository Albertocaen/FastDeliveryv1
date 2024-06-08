package org.proyecto.fastdeliveryp_v1.dto;

import java.lang.String;
import java.util.List;
import lombok.Data;
import org.proyecto.fastdeliveryp_v1.entity.Persona;

@Data
public class ClienteDto {
  private String dniCliente;

  private Persona persona;

  private String direccion;

  private String ciudad;

  private String codigoPostal;


}
