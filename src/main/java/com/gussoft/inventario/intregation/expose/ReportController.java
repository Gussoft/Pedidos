package com.gussoft.inventario.intregation.expose;

import com.gussoft.inventario.core.business.ReportService;
import com.gussoft.inventario.intregation.transfer.record.CustomerSould;
import com.gussoft.inventario.intregation.transfer.record.IProductStock;
import com.gussoft.inventario.intregation.transfer.record.ProductStock;
import com.gussoft.inventario.intregation.transfer.request.ReportRequest;
import com.gussoft.inventario.intregation.transfer.response.ReportResponse;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reportes")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

  private final ReportService reporteService;

  @GetMapping("/dashboard")
  public ResponseEntity<ReportResponse> obtenerDashboard(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

    ReportResponse dashboard = reporteService.obtenerDashboardVentas(fechaInicio, fechaFin);
    return ResponseEntity.ok(dashboard);
  }

  @GetMapping("/ventas/periodo")
  public ResponseEntity<ReportResponse> reporteVentasPorPeriodo(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

    ReportResponse reporte = reporteService.generarReporteVentasPorPeriodo(fechaInicio, fechaFin);
    return ResponseEntity.ok(reporte);
  }

  @GetMapping("/ventas/producto")
  public ResponseEntity<Page<Object[]>> reporteVentasPorProducto(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "totalVendido") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection) {

    Pageable pageable = PageRequest.of(page, size,
        Sort.by(Sort.Direction.fromString(sortDirection), sortBy));

    Page<Object[]> reporte = reporteService.reporteVentasPorProducto(fechaInicio, fechaFin, pageable);
    return ResponseEntity.ok(reporte);
  }

  @GetMapping("/ventas/categoria")
  public ResponseEntity<Page<IProductStock>> reporteVentasPorCategoria(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    Pageable pageable = PageRequest.of(page, size,
        Sort.by(Sort.Direction.DESC, "totalVentas"));

    Page<IProductStock> reporte = reporteService.reporteVentasPorCategoria(fechaInicio, fechaFin, pageable);
    return ResponseEntity.ok(reporte);
  }

  @GetMapping("/ventas/cliente")
  public ResponseEntity<Page<Object[]>> reporteVentasPorCliente(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    Pageable pageable = PageRequest.of(page, size,
        Sort.by(Sort.Direction.DESC, "totalGastado"));

    Page<Object[]> reporte = reporteService.reporteVentasPorCliente(fechaInicio, fechaFin, pageable);
    return ResponseEntity.ok(reporte);
  }

  @GetMapping("/productos/mas-vendidos")
  public ResponseEntity<Page<ProductStock>> reporteProductosMasVendidos(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    Pageable pageable = PageRequest.of(page, size,
        Sort.by(Sort.Direction.DESC, "totalVendido"));

    Page<ProductStock> reporte = reporteService.reporteProductosMasVendidos(fechaInicio, fechaFin, pageable);
    return ResponseEntity.ok(reporte);
  }

  @GetMapping("/productos/stock-bajo")
  public ResponseEntity<Page<Object[]>> reporteProductosStockBajo(
      @RequestParam(defaultValue = "10") Integer stockMinimo,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    Pageable pageable = PageRequest.of(page, size,
        Sort.by(Sort.Direction.ASC, "stock"));

    Page<Object[]> reporte = reporteService.reporteProductosStockBajo(stockMinimo, pageable);
    return ResponseEntity.ok(reporte);
  }

  @GetMapping("/clientes/frecuentes")
  public ResponseEntity<Page<Object[]>> reporteClientesMasFrecuentes(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    Pageable pageable = PageRequest.of(page, size,
        Sort.by(Sort.Direction.DESC, "totalPedidos"));

    Page<Object[]> reporte = reporteService.reporteClientesMasFrecuentes(fechaInicio, fechaFin, pageable);
    return ResponseEntity.ok(reporte);
  }

  @GetMapping("/clientes/mejor-compra")
  public ResponseEntity<Page<Object[]>> reporteClientesMejorCompra(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    Pageable pageable = PageRequest.of(page, size,
        Sort.by(Sort.Direction.DESC, "totalGastado"));

    Page<Object[]> reporte = reporteService.reporteClientesMejorCompra(fechaInicio, fechaFin, pageable);
    return ResponseEntity.ok(reporte);
  }

  @GetMapping("/clientes/top")
  public ResponseEntity<Page<CustomerSould>> reporteClientesTop(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    Pageable pageable = PageRequest.of(page, size,
        Sort.by(Sort.Direction.DESC, "totalGastado"));

    Page<CustomerSould> reporte = reporteService.reporteClientesTop(fechaInicio, fechaFin, pageable);
    return ResponseEntity.ok(reporte);
  }

  // Endpoint para exportar reportes (ejemplo adicional)
  @PostMapping("/exportar")
  public ResponseEntity<ReportResponse> exportarReporte(@Valid @RequestBody ReportRequest filtro) {

    // Aquí puedes implementar lógica más compleja con múltiples filtros
    ReportResponse reporte = reporteService.generarReporteVentasPorPeriodo(
        filtro.getFechaInicio(), filtro.getFechaFin());

    return ResponseEntity.ok(reporte);
  }

  // Endpoint para obtener métricas rápidas (sin paginación)
  @GetMapping("/metricas/rapidas")
  public ResponseEntity<ReportResponse> metricasRapidas(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
    ReportResponse metricas = reporteService.obtenerDashboardVentas(fechaInicio, fechaFin);
    return ResponseEntity.ok(metricas);
  }
}
