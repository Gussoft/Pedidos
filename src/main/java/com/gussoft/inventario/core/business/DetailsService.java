package com.gussoft.inventario.core.business;

import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.intregation.transfer.request.DetailRequest;
import com.gussoft.inventario.intregation.transfer.response.DetailResponse;
import java.util.List;

public interface DetailsService {

  Payloads<List<DetailResponse>> findAll(Long idPedido);

  DetailResponse findById(Long id);

  DetailResponse save(Long idPedido, DetailRequest request);

  DetailResponse update(Long id, DetailRequest request);

  void delete(Long id);

}
