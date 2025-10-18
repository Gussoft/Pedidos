package com.gussoft.inventario.intregation.transfer.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
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
public class DetailRequest {

  private Long detalleId;

  @JsonIgnore
  private OrderRequest pedido;

  private ProductRequest producto;

  @NotNull
  private Integer cantidad;

  @NotNull
  private BigDecimal precioUnitario;

  private BigDecimal subtotal;

}
