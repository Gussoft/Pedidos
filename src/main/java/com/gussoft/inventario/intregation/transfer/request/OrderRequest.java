package com.gussoft.inventario.intregation.transfer.request;

import java.math.BigDecimal;
import java.util.List;

import com.gussoft.inventario.core.models.OrderStatus;
import jakarta.validation.constraints.NotNull;
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
public class OrderRequest {

  private Long pedidoId;

  @NotNull
  private Long clienteId;

  private BigDecimal total;

  private String direccionEnvio;

  private String metodoPago;

  private OrderStatus estado;

  @NotNull
  List<DetailRequest> detalles;

}
