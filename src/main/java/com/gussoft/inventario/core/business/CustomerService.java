package com.gussoft.inventario.core.business;

import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.intregation.transfer.request.CustomerRequest;
import com.gussoft.inventario.intregation.transfer.response.CustomerResponse;
import java.util.List;
import java.util.Map;

public interface CustomerService {

  Payloads<List<CustomerResponse>> findAll(Integer numberPages, Integer numberRows, Map<String, String> filters);

  CustomerResponse findById(Long id);

  CustomerResponse save(CustomerRequest request);

  CustomerResponse update(Long id, CustomerRequest request);

  void delete(Long id);

}
