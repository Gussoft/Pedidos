package com.gussoft.inventario.intregation.transfer.response;

public interface ReporteCliente {

  Long getIdCliente();
  String getNombre();
  String getApellido();
  Long getTotalPedidos();
  Double getMontoTotal();

}
