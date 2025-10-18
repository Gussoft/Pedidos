package com.gussoft.inventario.intregation.transfer.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime fechaInicio;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime fechaFin;

  private Long categoriaId;
  private Long clienteId;
  private Long productoId;
  private String estadoPedido;
  private Integer stockMinimo;
  private Integer page;
  private Integer size;
  private String sortBy;
  private String sortDirection;

}
