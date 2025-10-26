package com.gussoft.inventario.intregation.transfer.record;

import java.math.BigDecimal;

public interface ClientesFrecuentes {

  Long getIdCliente();
  String getNombre();
  String getApellido();
  String getEmail();
  Integer getTotalPedidos();
  BigDecimal getTotalgastado();

}
