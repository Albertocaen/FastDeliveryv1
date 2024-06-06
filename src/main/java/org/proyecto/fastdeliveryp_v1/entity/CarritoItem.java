package org.proyecto.fastdeliveryp_v1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoItem {
    private Producto producto;
    private int cantidad;


    public Float getTotal() {
        return producto.getPrecio() * cantidad;
    }
}
