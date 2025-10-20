package com.gussoft.inventario.core.mappers;

import com.gussoft.inventario.core.models.Customer;
import com.gussoft.inventario.core.models.Order;
import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.core.util.DateUtils;
import com.gussoft.inventario.intregation.transfer.request.OrderRequest;
import com.gussoft.inventario.intregation.transfer.request.OrderUpdateRequest;
import com.gussoft.inventario.intregation.transfer.response.OrderResponse;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

@UtilityClass
public class OrderMapper {

  public static OrderResponse toResponse(Order data) {
    return OrderResponse.builder()
        .idPedido(data.getIdPedido())
        .total(data.getTotal())
        .fechaPedido(DateUtils.dateTimeFormater(data.getFechaPedido()))
        .cliente(CustomerMapper.toResponse(data.getCliente()))
        .direccionEnvio(data.getDireccionEnvio())
        .estado(String.valueOf(data.getEstado()))
        .metodoPago(data.getMetodoPago())
        .detalles(data.getDetalles().stream()
            .map(DetailsMapper::toResponse)
            .toList())
        .build();
  }

  public static Order toEntity(OrderRequest request) {
    return Order.builder()
        .total(request.getTotal())
        .metodoPago(request.getMetodoPago())
        .direccionEnvio(request.getDireccionEnvio())
        .estado(request.getEstado())
//        .cliente(Customer.builder().idCliente(request.getClienteId()).build())
//        .detalles(request.getDetalles().stream()
//            .map(DetailMapper::toEntity).toList())
        .build();
  }

  public static Order toUpdateEntity(Order data, OrderRequest request) {
    Optional.ofNullable(request.getTotal()).ifPresent(data::setTotal);
    Optional.ofNullable(request.getMetodoPago()).ifPresent(data::setMetodoPago);
    Optional.ofNullable(request.getEstado()).ifPresent(data::setEstado);
    Optional.ofNullable(request.getDireccionEnvio()).ifPresent(data::setDireccionEnvio);
    Optional.ofNullable(request.getClienteId())
        .ifPresent(id -> data.setCliente(Customer.builder().idCliente(id).build()));
    Optional.ofNullable(request.getDetalles())
        .ifPresent(details -> data.setDetalles(details.stream()
            .map(DetailsMapper::toEntity).toList()));
    return data;
  }

  public Order toUpdateEntity(Order existingOrder, OrderUpdateRequest request) {
    if (request.getDireccionEnvio() != null) {
      existingOrder.setDireccionEnvio(request.getDireccionEnvio());
    }
    if (request.getMetodoPago() != null) {
      existingOrder.setMetodoPago(request.getMetodoPago());
    }
    return existingOrder;
  }

  public static Payloads<List<OrderResponse>> toListResponse(Page<Order> content) {
    if (!content.hasContent()) {
      return new Payloads<>();
    }
    return toPayloads(content.map(OrderMapper::toResponse));
  }

  private static Payloads<List<OrderResponse>> toPayloads(Page<OrderResponse> data) {
    Payloads<List<OrderResponse>> response = new Payloads<>();
    response.setData(data.getContent());
    response.setTotalRegistros(data.getTotalElements());
    response.setPaginaActual(data.getPageable().getPageNumber());
    response.setTamanioPagina(data.getPageable().getPageSize());
    response.setTotalPaginas(data.getTotalPages());
    return response;
  }
}
