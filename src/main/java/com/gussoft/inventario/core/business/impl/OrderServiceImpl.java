package com.gussoft.inventario.core.business.impl;

import com.gussoft.inventario.core.business.OrderService;
import com.gussoft.inventario.core.exception.BusinessException;
import com.gussoft.inventario.core.exception.InsufficientStockException;
import com.gussoft.inventario.core.exception.InvalidStatusTransitionException;
import com.gussoft.inventario.core.exception.ResourceNotFoundException;
import com.gussoft.inventario.core.mappers.DetailsMapper;
import com.gussoft.inventario.core.mappers.OrderMapper;
import com.gussoft.inventario.core.models.Customer;
import com.gussoft.inventario.core.models.Details;
import com.gussoft.inventario.core.models.Order;
import com.gussoft.inventario.core.models.OrderStatus;
import com.gussoft.inventario.core.models.Product;
import com.gussoft.inventario.core.repository.CustomerRepository;
import com.gussoft.inventario.core.repository.DetailsRepository;
import com.gussoft.inventario.core.repository.OrderRepository;
import com.gussoft.inventario.core.repository.ProductRepository;
import com.gussoft.inventario.intregation.transfer.request.OrderRequest;
import com.gussoft.inventario.intregation.transfer.request.OrderUpdateRequest;
import com.gussoft.inventario.intregation.transfer.response.OrderResponse;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository repository;
  private final DetailsRepository detailsRepository;
  private final CustomerRepository customerRepository;
  private final ProductRepository productRepository;

  @Transactional
  @Override
  public OrderResponse save(OrderRequest request) {
    Customer customer = findByIdCustomer(request);
    Order order = OrderMapper.toEntity(request);
    order.setCliente(customer);
    Order data = repository.save(order);
    log.info("Order creada con ID: {}", data.getIdPedido());
    List<Details> details = request.getDetalles().stream()
        .map(dt -> {
          Details detail = DetailsMapper.toEntity(dt);
          Product product = findByIdProduct(dt.getProducto().getProductoId());
          validateStock(detail, product);
          detail.setPedido(data);
          detail.setProducto(product);
          detail.calcularSubtotal();
          return detail;
        }).toList();
    List<Details> response = detailsRepository.saveAll(details);
    data.setDetalles(response);
    BigDecimal totalSinIgv = details.stream()
        .map(Details::getSubtotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    BigDecimal igv = totalSinIgv.multiply(new BigDecimal("0.18"));
    log.info("IGV calculado para la Order ID {}: {}", data.getIdPedido(), igv);
    data.setTotal(totalSinIgv);
    Order save = repository.save(data);
    return OrderMapper.toResponse(save);
  }

  @Transactional
  @Override
  public OrderResponse update(Long id, OrderUpdateRequest request) {
    Order order = findEntityToId(id);

    if (request.getEstado() != null && !order.getEstado().equals(request.getEstado())) {
      OrderStatus estadoAnterior = order.getEstado();
      OrderStatus nuevoEstado = request.getEstado();
      if (!estadoAnterior.canTransitionTo(nuevoEstado)) {
        throw new InvalidStatusTransitionException(
            String.format("Transición no permitida: %s → %s", estadoAnterior, nuevoEstado));
      }
      executeStatusActions(order, estadoAnterior, nuevoEstado);
    }

    BigDecimal totalSinIgv = order.getDetalles().stream()
        .map(Details::getSubtotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    order.setTotal(totalSinIgv);
    Order orderToUpdate = OrderMapper.toUpdateEntity(order, request);
    Order updatedOrder = repository.save(orderToUpdate);

    return OrderMapper.toResponse(updatedOrder);
  }

  @Override
  public OrderResponse updateStatus(Long id, OrderStatus estado) {
    Order order = findEntityToId(id);
    OrderStatus estadoAnterior = order.getEstado();

    if (!estadoAnterior.canTransitionTo(estado)) {
      throw new InvalidStatusTransitionException(
          String.format("Transición no permitida: %s → %s", estadoAnterior, estado));
    }

    executeStatusActions(order, estadoAnterior, estado);
    order.setEstado(estado);
    Order updatedOrder = repository.save(order);

    return OrderMapper.toResponse(updatedOrder);
  }

  private void executeStatusActions(Order order, OrderStatus estadoAnterior, OrderStatus nuevoEstado) {
    switch (nuevoEstado) {
      case ENTREGADO:
        procesarEntrega(order);
        break;
      case CANCELADO:
        procesarCancelacion(order, estadoAnterior);
        break;
      case PENDIENTE:
        break;
    }
  }

  @Transactional
  @Override
  public void delete(Long id) {
    repository.delete(findEntityToId(id));
  }

  private Order findEntityToId(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> {
          String message = String.format("Order no encontrada con ID: %d", id);
          log.warn(message);
          return new ResourceNotFoundException(message);
        });
  }

  private Customer findByIdCustomer(OrderRequest request) {
    return customerRepository.findById(request.getClienteId())
        .orElseThrow(() -> {
          String message = String.format("Customer no encontrado con ID: %d", request.getClienteId());
          log.warn(message);
          return new ResourceNotFoundException(message);
        });
  }

  private Product findByIdProduct(Long id) {
    return productRepository.findById(id)
        .orElseThrow(() -> {
          String message = String.format("Product no encontrado con ID: %d", id);
          log.warn(message);
          return new ResourceNotFoundException(message);
        });
  }

  private void validateStock(Details details, Product product) {
    if (product.getStock() < details.getCantidad()) {
      String message = String.format("Stock insuficiente para el producto ID: %d", product.getIdProducto());
      log.warn(message);
      throw new BusinessException(message);
    }
  }

  private void procesarEntrega(Order order) {
    order.getDetalles().forEach(detail -> {
      Product product = detail.getProducto();
      int cantidadVendida = detail.getCantidad();

      if (product.getStock() < cantidadVendida) {
        throw new InsufficientStockException(
            String.format("Stock insuficiente para entregar el pedido. Producto: %s, Stock: %d, Solicitado: %d",
                product.getNombre(), product.getStock(), cantidadVendida));
      }

      product.setStock(product.getStock() - cantidadVendida);
      productRepository.save(product);

      log.debug("Stock actualizado para producto {}: -{} unidades",
          product.getNombre(), cantidadVendida);
    });
    log.info("Pedido {} marcado como ENTREGADO. Stocks actualizados.", order.getIdPedido());
  }

  private void procesarCancelacion(Order order, OrderStatus estadoAnterior) {
    log.info("Pedido {} cancelado. Estado anterior: {}", order.getIdPedido(), estadoAnterior);

    if (estadoAnterior == OrderStatus.PENDIENTE) {
      log.debug("Pedido cancelado en estado PENDIENTE, no se requiere liberar stock");
    }
  }

}
