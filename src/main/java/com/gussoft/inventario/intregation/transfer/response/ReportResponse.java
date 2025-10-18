package com.gussoft.inventario.intregation.transfer.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
public class ReportResponse {

  private String tipoReporte;
  private LocalDateTime fechaGeneracion;
  private LocalDateTime fechaInicio;
  private LocalDateTime fechaFin;
  private Map<String, Object> metricas;
  private List<?> datos;
  private Long totalRegistros;
  private BigDecimal totalVentas;
  private Integer totalPedidos;

}
