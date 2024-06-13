package org.proyecto.fastdeliveryp_v1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa un ítem en el carrito de compras.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoItem {
    private Producto producto;
    private int cantidad;

    /**
     * Calcula el total del ítem en el carrito basado en el precio del producto y la cantidad.
     *
     * @return El total del ítem en el carrito.
     */
    public Float getTotal() {
        return producto.getPrecio() * cantidad;
    }
}
