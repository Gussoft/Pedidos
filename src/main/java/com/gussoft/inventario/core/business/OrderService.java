package com.gussoft.inventario.core.business;

import com.gussoft.inventario.core.models.OrderStatus;
import com.gussoft.inventario.intregation.transfer.request.OrderRequest;
import com.gussoft.inventario.intregation.transfer.request.OrderUpdateRequest;
import com.gussoft.inventario.intregation.transfer.response.OrderResponse;

public interface OrderService {

  OrderResponse save(OrderRequest request);

  OrderResponse update(Long id, OrderUpdateRequest request);

  OrderResponse updateStatus(Long id, OrderStatus estado);

  void delete(Long id);

}
