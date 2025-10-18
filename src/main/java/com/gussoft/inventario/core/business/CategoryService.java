package com.gussoft.inventario.core.business;

import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.intregation.transfer.request.CategoryRequest;
import com.gussoft.inventario.intregation.transfer.response.CategoryResponse;
import java.util.List;
import java.util.Map;

public interface CategoryService {

  Payloads<List<CategoryResponse>> findAll(Integer numberPages, Integer numberRows, Map<String, String> filters);

  CategoryResponse findById(Long id);

  CategoryResponse save(CategoryRequest request);

  CategoryResponse update(Long id, CategoryRequest request);

  void delete(Long id);

}
