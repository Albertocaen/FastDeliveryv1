package org.proyecto.fastdeliveryp_v1.dto;

import java.lang.String;
import java.util.List;
import lombok.Data;
import org.proyecto.fastdeliveryp_v1.entity.Persona;

@Data
public class AdminDto {
  private String dniAdmin;

  private Persona persona;

  private List stocks;

  private List pedidosProveedor;
}
