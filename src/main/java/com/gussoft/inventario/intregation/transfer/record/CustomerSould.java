package com.gussoft.inventario.intregation.transfer.record;

import java.math.BigDecimal;

public interface CustomerSould {

    Long getIdCliente();
    String getNombre();
    String getApellido();
    String getEmail();
    Integer getTotalPedidos();
    BigDecimal totalGastado();

}
