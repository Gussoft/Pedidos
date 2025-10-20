package com.gussoft.inventario.core.mappers;

import com.gussoft.inventario.core.models.Details;
import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.intregation.transfer.request.DetailRequest;
import com.gussoft.inventario.intregation.transfer.response.DetailResponse;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DetailsMapper {

  public static DetailResponse toResponse(Details data) {
    return DetailResponse.builder()
        .idDetalle(data.getIdDetalle())
        .cantidad(data.getCantidad())
        .subtotal(data.getSubtotal())
        .precioUnitario(data.getPrecioUnitario())
        .producto(ProductMapper.toResponse(data.getProducto()))
        .build();
  }

  public static Details toEntity(DetailRequest request) {
    return Details.builder()
        .cantidad(request.getCantidad())
        .subtotal(request.getSubtotal())
        .precioUnitario(request.getPrecioUnitario())
        .producto(ProductMapper.toEntity(request.getProducto()))
//        .pedido(OrderMapper.toEntity(request.getPedido()))
        .build();
  }

  public static Details toUpdateEntity(Details data, DetailRequest request) {
    Optional.ofNullable(request.getCantidad()).ifPresent(data::setCantidad);
    Optional.ofNullable(request.getPrecioUnitario()).ifPresent(data::setPrecioUnitario);
    Optional.ofNullable(request.getSubtotal()).ifPresent(data::setSubtotal);

    Optional.ofNullable(request.getProducto())
        .map(ProductMapper::toEntity)
        .ifPresent(data::setProducto);
    Optional.ofNullable(request.getPedido())
         .map(OrderMapper::toEntity)
         .ifPresent(data::setPedido);
    return data;
  }

  public static Payloads<List<DetailResponse>> toListResponse(List<Details> details) {
      List<DetailResponse> data = details.stream()
            .map(DetailsMapper::toResponse)
            .toList();
      Payloads<List<DetailResponse>> response = new Payloads<>();
      response.setData(data);
      return response;
  }

}
