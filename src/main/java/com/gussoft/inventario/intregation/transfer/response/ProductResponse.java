package com.gussoft.inventario.intregation.transfer.response;

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
public class ProductResponse {

  private Long idProducto;

  private CategoryResponse categoria;

  private String nombre;

  private String descripcion;

  private BigDecimal precio;

  private Integer stock;

  private String modelo;

  private String marca;

  private String color;

  private String fechaCreacion;

  private Boolean estado;

}
