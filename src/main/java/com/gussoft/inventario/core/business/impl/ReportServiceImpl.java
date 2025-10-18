package com.gussoft.inventario.core.business.impl;

import com.gussoft.inventario.core.business.ReportService;
import com.gussoft.inventario.core.mappers.OrderMapper;
import com.gussoft.inventario.core.models.Order;
import com.gussoft.inventario.core.repository.CategoryRepository;
import com.gussoft.inventario.core.repository.CustomerRepository;
import com.gussoft.inventario.core.repository.DetailsRepository;
import com.gussoft.inventario.core.repository.OrderRepository;
import com.gussoft.inventario.core.repository.ProductRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import com.gussoft.inventario.core.repository.specification.OrderSpecification;
import com.gussoft.inventario.intregation.transfer.record.CustomerSould;
import com.gussoft.inventario.intregation.transfer.record.IProductStock;
import com.gussoft.inventario.intregation.transfer.record.ProductStock;
import com.gussoft.inventario.intregation.transfer.response.OrderResponse;
import com.gussoft.inventario.intregation.transfer.response.ReportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

  private final OrderRepository orderRepository;
  private final DetailsRepository detailsRepository;
  private final ProductRepository productRepository;
  private final CustomerRepository customerRepository;
  private final CategoryRepository categoryRepository;

  @Override
  public ReportResponse obtenerDashboardVentas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
    log.info("Generando dashboard de ventas desde {} hasta {}", fechaInicio, fechaFin);

    // Crear specification para el período
    Specification<Order> spec = OrderSpecification.crearSpecificationVentasPorPeriodo(fechaInicio, fechaFin);

    // Obtener métricas usando el repository directamente
    List<Order> pedidos = orderRepository.findAll(spec);

    // Calcular métricas manualmente
    Long totalPedidos = (long) pedidos.size();

    BigDecimal totalVentas = pedidos.stream()
        .map(Order::getTotal)
        .filter(Objects::nonNull)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal ticketPromedio = totalPedidos > 0 ?
        totalVentas.divide(BigDecimal.valueOf(totalPedidos), 2, RoundingMode.HALF_UP) :
        BigDecimal.ZERO;

    Long clientesUnicos = pedidos.stream()
        .map(order -> order.getCliente().getIdCliente())
        .distinct()
        .count();

    // ... resto del código igual
    Map<String, Object> metricas = new HashMap<>();
    metricas.put("totalPedidos", totalPedidos);
    metricas.put("totalVentas", totalVentas);
    metricas.put("ticketPromedio", ticketPromedio);
    metricas.put("clientesUnicos", clientesUnicos);

    return ReportResponse.builder()
        .tipoReporte("DASHBOARD_VENTAS")
        .fechaGeneracion(LocalDateTime.now())
        .fechaInicio(fechaInicio)
        .fechaFin(fechaFin)
        .metricas(metricas)
        .totalVentas(totalVentas)
        .totalPedidos(totalPedidos.intValue())
        .build();
  }

  @Override
  public Page<Object[]> reporteVentasPorProducto(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable) {
    return detailsRepository.findVentasPorProducto(fechaInicio, fechaFin, pageable);
  }

  @Override
  public Page<IProductStock> reporteVentasPorCategoria(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable) {
    return detailsRepository.findVentasPorCategoria(fechaInicio, fechaFin, pageable);
  }

  @Override
  public Page<Object[]> reporteVentasPorCliente(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable) {
    return orderRepository.findVentasPorCliente(fechaInicio, fechaFin, pageable);
  }

  @Override
  public Page<ProductStock> reporteProductosMasVendidos(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable) {
    return detailsRepository.findProductosMasVendidos(fechaInicio, fechaFin, pageable);
  }

  @Override
  public Page<Object[]> reporteProductosStockBajo(Integer stockMinimo, Pageable pageable) {
    return productRepository.findProductosStockBajo(stockMinimo, pageable);
  }

  @Override
  public Page<Object[]> reporteClientesMasFrecuentes(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable) {
    return orderRepository.findClientesMasFrecuentes(fechaInicio, fechaFin, pageable);
  }

  @Override
  public Page<Object[]> reporteClientesMejorCompra(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable) {
    return orderRepository.findClientesMejorCompra(fechaInicio, fechaFin, pageable);
  }

  @Override
  public Page<CustomerSould> reporteClientesTop(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable) {
    return orderRepository.findClientesTop(fechaInicio, fechaFin, pageable);
  }

  @Override
  public ReportResponse generarReporteVentasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
    log.info("Generando reporte detallado de ventas desde {} hasta {}", fechaInicio, fechaFin);

    Specification<Order> spec = OrderSpecification.crearSpecificationVentasPorPeriodo(fechaInicio, fechaFin);


    List<Order> pedidos = orderRepository.findAll(spec);
    List<OrderResponse> pedidosDTO = pedidos.stream()
        .map(OrderMapper::toResponse)
        .collect(Collectors.toList());

    BigDecimal totalVentas = pedidos.stream()
        .map(Order::getTotal)
        .filter(Objects::nonNull)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal ticketPromedio = pedidos.isEmpty() ? BigDecimal.ZERO :
        totalVentas.divide(BigDecimal.valueOf(pedidos.size()), 2, RoundingMode.HALF_UP);

    Map<String, BigDecimal> ventasPorMetodoPago = pedidos.stream()
        .collect(Collectors.groupingBy(
            Order::getMetodoPago,
            Collectors.reducing(BigDecimal.ZERO, Order::getTotal, BigDecimal::add)
        ));

    Map<String, Long> pedidosPorEstado = pedidos.stream()
        .collect(Collectors.groupingBy(
            order -> order.getEstado().name(),
            Collectors.counting()
        ));

    Map<String, Object> metricas = new HashMap<>();
    metricas.put("totalPedidos", pedidos.size());
    metricas.put("totalVentas", totalVentas);
    metricas.put("ticketPromedio", ticketPromedio);
    metricas.put("ventasPorMetodoPago", ventasPorMetodoPago);
    metricas.put("pedidosPorEstado", pedidosPorEstado);
    metricas.put("rangoFechas", fechaInicio + " - " + fechaFin);

    return ReportResponse.builder()
        .tipoReporte("REPORTE_VENTAS_PERIODO")
        .fechaGeneracion(LocalDateTime.now())
        .fechaInicio(fechaInicio)
        .fechaFin(fechaFin)
        .metricas(metricas)
        .datos(pedidosDTO)
        .totalVentas(totalVentas)
        .totalPedidos(pedidos.size())
        .build();
  }

  public Page<Object[]> reporteClientesConTotalGastado(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable) {
    return customerRepository.findClientesConTotalGastado(fechaInicio, fechaFin, pageable);
  }

}
