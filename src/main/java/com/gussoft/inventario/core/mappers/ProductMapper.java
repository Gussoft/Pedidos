package com.gussoft.inventario.core.mappers;

import com.gussoft.inventario.core.models.Category;
import com.gussoft.inventario.core.models.Product;
import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.core.util.DateUtils;
import com.gussoft.inventario.intregation.transfer.request.ProductRequest;
import com.gussoft.inventario.intregation.transfer.response.CategoryResponse;
import com.gussoft.inventario.intregation.transfer.response.ProductResponse;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

@UtilityClass
public class ProductMapper {

  public static ProductResponse toResponse(Product data) {
    String dateStr = DateUtils.dateTimeFormater(DateUtils.fechaLima());
    return ProductResponse.builder()
        .idProducto(data.getIdProducto())
        .nombre(data.getNombre())
        .descripcion(data.getDescripcion())
        .precio(data.getPrecio())
        .stock(data.getStock())
        .modelo(data.getModelo())
        .marca(data.getMarca())
        .color(data.getColor())
        .fechaCreacion(data.getFechaCreacion() != null ? DateUtils.dateTimeFormater(data.getFechaCreacion()) : dateStr)
        .estado(data.getEstado())
        .categoria(CategoryResponse.builder()
            .idCategoria(data.getCategoria().getIdCategoria())
            .nombre(data.getCategoria().getNombre()).build())
        .build();
  }

  public static Product toEntity(ProductRequest request) {
    return Product.builder()
        .idProducto(request.getProductoId())
        .nombre(request.getNombre())
        .descripcion(request.getDescripcion())
        .precio(request.getPrecio())
        .stock(request.getStock())
        .modelo(request.getModelo())
        .marca(request.getMarca())
        .color(request.getColor())
        //.fechaCreacion(DateUtils.parseStringToDate(request.getFechaCreacion()))
        .estado(request.getEstado())
        .categoria(Category.builder().idCategoria(request.getCategoriaId()).build())
        .build();
  }

  public static Product toUpdateEntity(Product data, ProductRequest request) {
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

  public static Payloads<List<ProductResponse>> toListResponse(Page<Product> content) {
    if (!content.hasContent()) {
      return new Payloads<>();
    }
    return toPayloads(content.map(ProductMapper::toResponse));
  }

  private static Payloads<List<ProductResponse>> toPayloads(Page<ProductResponse> data) {
    Payloads<List<ProductResponse>> response = new Payloads<>();
    response.setData(data.getContent());
    response.setTotalRegistros(data.getTotalElements());
    response.setPaginaActual(data.getPageable().getPageNumber());
    response.setTamanioPagina(data.getPageable().getPageSize());
    response.setTotalPaginas(data.getTotalPages());
    return response;
  }
}
