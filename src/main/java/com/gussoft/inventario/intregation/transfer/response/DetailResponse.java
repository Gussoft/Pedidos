package com.gussoft.inventario.intregation.transfer.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class DetailResponse {

  private Long idDetalle;

  @JsonIgnore
  private OrderResponse pedido;

  private ProductResponse producto;

  private Integer cantidad;

  private BigDecimal precioUnitario;

  private BigDecimal subtotal;

}
