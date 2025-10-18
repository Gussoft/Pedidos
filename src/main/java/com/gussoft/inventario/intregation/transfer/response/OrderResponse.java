package com.gussoft.inventario.intregation.transfer.response;

import java.math.BigDecimal;
import java.util.List;
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
public class OrderResponse {

  private Long idPedido;

  private CustomerResponse cliente;

  private String fechaPedido;

  private BigDecimal total;

  private String direccionEnvio;

  private String metodoPago;

  private String estado;

  List<DetailResponse> detalles;

}
