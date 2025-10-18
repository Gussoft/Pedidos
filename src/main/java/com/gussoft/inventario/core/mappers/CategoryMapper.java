package com.gussoft.inventario.core.mappers;

import com.gussoft.inventario.core.models.Category;
import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.core.util.DateUtils;
import com.gussoft.inventario.intregation.transfer.request.CategoryRequest;
import com.gussoft.inventario.intregation.transfer.response.CategoryResponse;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

@UtilityClass
public class CategoryMapper {

  public static CategoryResponse toResponse(Category data) {
    return CategoryResponse.builder()
        .idCategoria(data.getIdCategoria())
        .nombre(data.getNombre())
        .descripcion(data.getDescripcion())
        .fechaCreacion(DateUtils.dateTimeFormater(data.getFechaCreacion()))
        .activo(data.getEstado())
        .build();
  }

  public static Category toEntity(CategoryRequest request) {
    return Category.builder()
        .nombre(request.getNombre())
        .descripcion(request.getDescripcion())
        .estado(request.getEstado())
        .build();
  }

  public static Category toUpdateEntity(Category data, CategoryRequest request) {
    Optional.ofNullable(request.getNombre()).ifPresent(data::setNombre);
    Optional.ofNullable(request.getDescripcion()).ifPresent(data::setDescripcion);
    Optional.ofNullable(request.getEstado()).ifPresent(data::setEstado);
    return data;
  }

  public static Payloads<List<CategoryResponse>> toListResponse(Page<Category> content) {
    if (!content.hasContent()) {
      return new Payloads<>();
    }
    return toPayloads(content.map(CategoryMapper::toResponse));
  }

  private static Payloads<List<CategoryResponse>> toPayloads(Page<CategoryResponse> data) {
    Payloads<List<CategoryResponse>> response = new Payloads<>();
    response.setData(data.getContent());
    response.setTotalRegistros(data.getTotalElements());
    response.setPaginaActual(data.getPageable().getPageNumber());
    response.setTamanioPagina(data.getPageable().getPageSize());
    response.setTotalPaginas(data.getTotalPages());
    return response;
  }
}
