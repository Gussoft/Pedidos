package com.gussoft.inventario.intregation.transfer.record;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStock {

  private Long idCategoria;
  private String nombre;
  private String marca;
  private String modelo;
  private Integer stock;
  private BigDecimal precio;
  private String categoria;

}
