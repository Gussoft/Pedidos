package com.gussoft.inventario.intregation.transfer.record;

import java.math.BigDecimal;

public record ProductStock (
    Long idProducto,
    String nombre,
    String marca,
    String modelo,
    Integer stock,
    BigDecimal precio,
    String categoria) {
}
