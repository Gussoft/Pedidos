package com.gussoft.inventario.intregation.transfer.record;

import java.math.BigDecimal;

public interface VentasProductos {

  Long getIdProducto();
  String getNombre();
  String getMarca();
  String getModelo();
  Integer getTotalVendido();
  BigDecimal getTotalVentas();

}
