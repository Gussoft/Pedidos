package com.gussoft.inventario.core.mappers;

import com.gussoft.inventario.core.models.Category;
import com.gussoft.inventario.core.models.Details;
import com.gussoft.inventario.core.models.Order;
import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.core.util.DateUtils;
import com.gussoft.inventario.intregation.transfer.request.DetailRequest;
import com.gussoft.inventario.intregation.transfer.response.DetailResponse;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

@UtilityClass
public class DetailMapper {

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

  /*
  public static Details toUpdateEntity(Details data, DetailRequest request) {
    Optional.ofNullable(request.getNombre()).ifPresent(data::setNombre);
    Optional.ofNullable(request.getDescripcion()).ifPresent(data::setDescripcion);
    Optional.ofNullable(request.getPrecio()).ifPresent(data::setPrecio);
    Optional.ofNullable(request.getStock()).ifPresent(data::setStock);
    Optional.ofNullable(request.getModelo()).ifPresent(data::setModelo);
    Optional.ofNullable(request.getMarca()).ifPresent(data::setMarca);
    Optional.ofNullable(request.getColor()).ifPresent(data::setColor);
    Optional.ofNullable(request.getEstado()).ifPresent(data::setEstado);
    Optional.ofNullable(request.getFechaCreacion()).ifPresent(date ->
        data.setFechaCreacion(DateUtils.parseStringToDate(date)));
    return data;
  }

   */

  public static Payloads<List<DetailResponse>> toListResponse(Page<Details> content) {
    if (!content.hasContent()) {
      return new Payloads<>();
    }
    return toPayloads(content.map(DetailMapper::toResponse));
  }

  private static Payloads<List<DetailResponse>> toPayloads(Page<DetailResponse> data) {
    Payloads<List<DetailResponse>> response = new Payloads<>();
    response.setData(data.getContent());
    response.setTotalRegistros(data.getTotalElements());
    response.setPaginaActual(data.getPageable().getPageNumber());
    response.setTamanioPagina(data.getPageable().getPageSize());
    response.setTotalPaginas(data.getTotalPages());
    return response;
  }
}
