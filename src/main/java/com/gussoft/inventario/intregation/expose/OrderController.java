package com.gussoft.inventario.intregation.expose;

import com.gussoft.inventario.core.business.OrderService;
import com.gussoft.inventario.core.models.OrderStatus;
import com.gussoft.inventario.core.models.common.Payload;
import com.gussoft.inventario.intregation.transfer.request.OrderRequest;
import com.gussoft.inventario.intregation.transfer.request.OrderUpdateRequest;
import com.gussoft.inventario.intregation.transfer.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class OrderController {

  private final OrderService service;

  @PostMapping("/pedidos")
  public ResponseEntity<Payload<OrderResponse>> create(@Valid @RequestBody OrderRequest request) {
    OrderResponse response = service.save(request);
    return new ResponseEntity<>(new Payload<>(response), HttpStatus.CREATED);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/pedidos/{id}")
  public ResponseEntity<Payload<OrderResponse>> update(
      @PathVariable Long id,
      @Valid @RequestBody OrderUpdateRequest request) {
    OrderResponse response = service.update(id, request);
    return ResponseEntity.ok(new Payload<>(response));
  }

  @DeleteMapping("/pedidos/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/pedidos/{id}/estado")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Payload<OrderResponse>> updateStatus(
      @PathVariable Long id,
      @RequestParam OrderStatus estado) {
    OrderResponse response = service.updateStatus(id, estado);
    return ResponseEntity.ok(new Payload<>(response));
  }

}
