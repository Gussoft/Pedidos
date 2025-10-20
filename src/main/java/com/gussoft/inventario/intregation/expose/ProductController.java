package com.gussoft.inventario.intregation.expose;

import com.gussoft.inventario.core.business.ProductService;
import com.gussoft.inventario.core.models.common.Payload;
import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.intregation.transfer.request.ProductRequest;
import com.gussoft.inventario.intregation.transfer.response.ProductResponse;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class ProductController {

  private final ProductService service;

  @GetMapping("/productos")
  public ResponseEntity<Payloads<List<ProductResponse>>> findAllWithFilters(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false) String nombre,
      @RequestParam(required = false) String descripcion,
      @RequestParam(required = false) String stock,
      @RequestParam(required = false) String precio,
      @RequestParam(required = false) String fechaRegistro,
      @RequestParam(required = false, defaultValue = "true") Boolean estado){
    Map<String, String> filters = setFilters(nombre, descripcion, stock, precio,fechaRegistro, estado);
    Payloads<List<ProductResponse>> response = service.findAll(page, size, filters);
    if (response.getData() == null) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(response);
  }

  @GetMapping("/productos/{id}")
  public ResponseEntity<Payload<ProductResponse>> findById(@PathVariable Long id) {
    ProductResponse response = service.findById(id);
    return ResponseEntity.ok(new Payload<>(response));
  }

  @PostMapping("/productos")
  public ResponseEntity<Payload<ProductResponse>> create(@Valid @RequestBody ProductRequest request) {
    ProductResponse response = service.save(request);
    return new ResponseEntity<>(new Payload<>(response), HttpStatus.CREATED);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/productos/{id}")
  public ResponseEntity<Payload<ProductResponse>> update(
      @PathVariable Long id,
      @Valid @RequestBody ProductRequest request) {
    ProductResponse response = service.update(id, request);
    return ResponseEntity.ok(new Payload<>(response));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/productos/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  private static Map<String, String> setFilters(String nombre, String descripcion,String stock,
                                                String precio, String fechaRegistro, Boolean estado) {
    Map<String, String> filters = new HashMap<>();
    filters.put("nombre", nombre);
    filters.put("descripcion", descripcion);
    filters.put("stock", stock);
    filters.put("precio", precio);
    filters.put("fechaRegistro", fechaRegistro);
    filters.put("estado", String.valueOf(estado));
    return filters;
  }

}
