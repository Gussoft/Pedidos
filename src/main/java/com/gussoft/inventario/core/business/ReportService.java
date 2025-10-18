package com.gussoft.inventario.core.business;

import com.gussoft.inventario.intregation.transfer.record.CustomerSould;
import com.gussoft.inventario.intregation.transfer.record.IProductStock;
import com.gussoft.inventario.intregation.transfer.record.ProductStock;
import com.gussoft.inventario.intregation.transfer.response.ReportResponse;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {

  ReportResponse generarReporteVentasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin);
  Page<Object[]> reporteVentasPorProducto(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
  Page<IProductStock> reporteVentasPorCategoria(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
  Page<Object[]> reporteVentasPorCliente(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);

  // Reportes de Productos
  Page<ProductStock> reporteProductosMasVendidos(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
  Page<Object[]> reporteProductosStockBajo(Integer stockMinimo, Pageable pageable);

  // Reportes de Clientes
  Page<Object[]> reporteClientesMasFrecuentes(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
  Page<Object[]> reporteClientesMejorCompra(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
  Page<CustomerSould> reporteClientesTop(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);

  // Dashboard/Resumen
  ReportResponse obtenerDashboardVentas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

}
