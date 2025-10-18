package com.gussoft.inventario.intregation.transfer.record;

import java.math.BigDecimal;

public interface IProductStock {

  Long getIdCategoria();
  String getNombre();
  Long getTotalUnidades();
  BigDecimal getTotalVentas();

}
