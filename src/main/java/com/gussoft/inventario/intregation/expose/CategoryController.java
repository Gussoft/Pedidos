package com.gussoft.inventario.intregation.expose;

import com.gussoft.inventario.core.business.CategoryService;
import com.gussoft.inventario.core.models.common.Payload;
import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.intregation.transfer.request.CategoryRequest;
import com.gussoft.inventario.intregation.transfer.response.CategoryResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {

  private final CategoryService service;

  @GetMapping("/categorias")
  public ResponseEntity<Payloads<List<CategoryResponse>>> findAllWithFilters(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false) String nombre,
      @RequestParam(required = false, defaultValue = "true") Boolean estado){
    Map<String, String> filters = new HashMap<>();
    filters.put("nombre", nombre);
    filters.put("estado", String.valueOf(estado));
    Payloads<List<CategoryResponse>> response = service.findAll(page, size, filters);
    if (response.getData() == null) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(response);
  }

  @GetMapping("categorias/{id}")
  public ResponseEntity<Payload<CategoryResponse>> findById(@PathVariable Long id) {
    CategoryResponse response = service.findById(id);
    return ResponseEntity.ok(new Payload<>(response));
  }

  @PostMapping("/categorias")
  public ResponseEntity<Payload<CategoryResponse>> create(@Valid @RequestBody CategoryRequest request) {
    CategoryResponse response = service.save(request);
    return new ResponseEntity<>(new Payload<>(response), HttpStatus.CREATED);
  }

  @PutMapping("/categorias/{id}")
  public ResponseEntity<Payload<CategoryResponse>> update(
      @PathVariable Long id,
      @Valid @RequestBody CategoryRequest request) {
    CategoryResponse response = service.update(id, request);
    return ResponseEntity.ok(new Payload<>(response));
  }

  @DeleteMapping("/categorias/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

}
