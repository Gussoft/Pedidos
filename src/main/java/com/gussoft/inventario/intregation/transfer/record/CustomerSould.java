package com.gussoft.inventario.intregation.transfer.record;

import java.math.BigDecimal;

public record CustomerSould (
    Long idCliente,
    String nombre,
    String apellido,
    String email,
    Long totalPedidos,
    BigDecimal totalGastado) {

}
