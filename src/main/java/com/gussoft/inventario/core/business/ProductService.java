package com.gussoft.inventario.core.business;

import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.intregation.transfer.request.ProductRequest;
import com.gussoft.inventario.intregation.transfer.response.ProductResponse;
import java.util.List;
import java.util.Map;

public interface ProductService {

  Payloads<List<ProductResponse>> findAll(Integer numberPages, Integer numberRows, Map<String, String> filters);

  ProductResponse findById(Long id);

  ProductResponse save(ProductRequest request);

  ProductResponse update(Long id, ProductRequest request);

  void delete(Long id);

}
