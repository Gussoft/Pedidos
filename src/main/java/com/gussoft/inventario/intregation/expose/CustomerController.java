package com.gussoft.inventario.intregation.expose;

import com.gussoft.inventario.core.business.CustomerService;
import com.gussoft.inventario.core.models.common.Payload;
import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.intregation.transfer.request.CustomerRequest;
import com.gussoft.inventario.intregation.transfer.response.CustomerResponse;
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
public class CustomerController {

  private final CustomerService service;

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/clientes")
  public ResponseEntity<Payloads<List<CustomerResponse>>> findAllWithFilters(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false) String nombre,
      @RequestParam(required = false) String apellido,
      @RequestParam(required = false) String dni,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) String telefono,
      @RequestParam(required = false) String fechaRegistro,
      @RequestParam(required = false) String direccion,
      @RequestParam(required = false, defaultValue = "true") Boolean estado){
    Map<String, String> filters = setFilters(nombre, apellido, dni, email, telefono, fechaRegistro, direccion, estado);
    Payloads<List<CustomerResponse>> response = service.findAll(page, size, filters);
    if (response.getData() == null) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(response);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/clientes/{id}")
  public ResponseEntity<Payload<CustomerResponse>> findById(@PathVariable Long id) {
    CustomerResponse response = service.findById(id);
    return ResponseEntity.ok(new Payload<>(response));
  }

  @PostMapping("/clientes")
  public ResponseEntity<Payload<CustomerResponse>> create(@Valid @RequestBody CustomerRequest request) {
    CustomerResponse response = service.save(request);
    return new ResponseEntity<>(new Payload<>(response), HttpStatus.CREATED);
  }

  @PutMapping("/clientes/{id}")
  public ResponseEntity<Payload<CustomerResponse>> update(
      @PathVariable Long id,
      @Valid @RequestBody CustomerRequest request) {
    CustomerResponse response = service.update(id, request);
    return ResponseEntity.ok(new Payload<>(response));
  }

  @DeleteMapping("/clientes/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  private static Map<String, String> setFilters(String nombre, String apellido, String dni, String email,
                                                String telefono, String fechaRegistro, String direccion, Boolean estado) {
    Map<String, String> filters = new HashMap<>();
    filters.put("nombre", nombre);
    filters.put("apellido", apellido);
    filters.put("dni", dni);
    filters.put("email", email);
    filters.put("telefono", telefono);
    filters.put("fechaRegistro", fechaRegistro);
    filters.put("direccion", direccion);
    filters.put("estado", String.valueOf(estado));
    return filters;
  }

}
