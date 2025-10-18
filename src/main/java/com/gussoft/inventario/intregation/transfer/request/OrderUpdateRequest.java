package com.gussoft.inventario.intregation.transfer.request;

import com.gussoft.inventario.core.models.OrderStatus;
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
public class OrderUpdateRequest {

  private String direccionEnvio;

  private String metodoPago;

  private OrderStatus estado;

}
