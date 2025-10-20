package com.gussoft.inventario.core.business.impl;

import com.gussoft.inventario.core.business.DetailsService;
import com.gussoft.inventario.core.exception.ResourceNotFoundException;
import com.gussoft.inventario.core.mappers.DetailsMapper;
import com.gussoft.inventario.core.models.Details;
import com.gussoft.inventario.core.models.Order;
import com.gussoft.inventario.core.models.Product;
import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.core.repository.DetailsRepository;
import com.gussoft.inventario.core.repository.OrderRepository;
import com.gussoft.inventario.core.repository.ProductRepository;
import com.gussoft.inventario.intregation.transfer.request.DetailRequest;
import com.gussoft.inventario.intregation.transfer.response.DetailResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetailsServiceImpl implements DetailsService {

  private final DetailsRepository repository;
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;

  @Override
  public Payloads<List<DetailResponse>> findAll(Long idPedido) {

    List<Details> detailsList = repository.findByPedidoId(idPedido);
    Payloads<List<DetailResponse>> data = DetailsMapper.toListResponse(detailsList);
    if (data.getData() == null) {
      log.warn("No se encontraron detalles");
      return new Payloads<>();
    }
    return data;
  }

  @Override
  public DetailResponse findById(Long id) {
    return DetailsMapper.toResponse(findEntityToId(id));
  }

  @Transactional
  @Override
  public DetailResponse save(Long idPedido, DetailRequest request) {
    Order order = setPedidoById(idPedido);
    Details details = DetailsMapper.toEntity(request);
    Product product = setProductById(details.getProducto().getIdProducto());
    details.setPedido(order);
    details.setProducto(product);
    Details data = repository.save(details);
    log.info("Details creada con ID: {}", data.getIdDetalle());
    return DetailsMapper.toResponse(data);
  }

  @Transactional
  @Override
  public DetailResponse update(Long id, DetailRequest request) {
    Product product = setProductById(request.getProducto().getProductoId());
    Details details = DetailsMapper.toUpdateEntity(findEntityToId(id), request);
    details.setProducto(product);
    Details data = repository.save(details);
    log.info("Details actualizada con ID: {}", data.getIdDetalle());
    return DetailsMapper.toResponse(data);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    repository.delete(findEntityToId(id));
  }

  private Details findEntityToId(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> {
          String message = String.format("Details no encontrado con ID: %d", id);
          log.warn(message);
          return new ResourceNotFoundException(message);
        });
  }

  private Order setPedidoById(Long idPedido) {
    return orderRepository.findById(idPedido)
        .orElseThrow(() -> {
          String message = String.format("Order no encontrado con ID: %d", idPedido);
          log.warn(message);
          return new ResourceNotFoundException(message);
        });
  }

  private Product setProductById(Long idProduct) {
    return productRepository.findById(idProduct)
        .orElseThrow(() -> {
          String message = String.format("Producto no encontrado con ID: %d", idProduct);
          log.warn(message);
          return new ResourceNotFoundException(message);
        });
  }

}
