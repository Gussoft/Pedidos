package com.gussoft.inventario.intregation.expose;

import com.gussoft.inventario.core.business.DetailsService;
import com.gussoft.inventario.core.business.OrderService;
import com.gussoft.inventario.core.models.common.Payload;
import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.intregation.transfer.request.DetailRequest;
import com.gussoft.inventario.intregation.transfer.response.DetailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class DetailsController {

  private final DetailsService detailService;
  private final OrderService orderService;

  @PostMapping("/pedidos/{idPedido}/detalles")
  public ResponseEntity<Payload<DetailResponse>> create(
      @PathVariable Long idPedido,
      @Valid @RequestBody DetailRequest request) {

    DetailResponse response = detailService.save(idPedido, request);
    return new ResponseEntity<>(new Payload<>(response), HttpStatus.CREATED);
  }

  @GetMapping("/pedidos/{idPedido}/detalles")
  public ResponseEntity<Payloads<List<DetailResponse>>> listByOrder(@PathVariable Long idPedido) {
    Payloads<List<DetailResponse>> response = detailService.findAll(idPedido);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/detalles/{idDetalle}")
  public ResponseEntity<Payload<DetailResponse>> update(
      @PathVariable Long idDetalle,
      @Valid @RequestBody DetailRequest request) {

    DetailResponse response = detailService.update(idDetalle, request);
    return ResponseEntity.ok(new Payload<>(response));
  }

  @DeleteMapping("/detalles/{idDetalle}")
  @PreAuthorize("@detailSecurity.canDeleteDetail(authentication, #idDetalle)")
  public ResponseEntity<Void> delete(@PathVariable Long idDetalle) {
    detailService.delete(idDetalle);
    return ResponseEntity.noContent().build();
  }
}
