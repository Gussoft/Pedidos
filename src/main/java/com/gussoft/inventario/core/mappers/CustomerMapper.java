package com.gussoft.inventario.core.mappers;

import com.gussoft.inventario.core.models.Customer;
import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.core.util.DateUtils;
import com.gussoft.inventario.intregation.transfer.request.CustomerRequest;
import com.gussoft.inventario.intregation.transfer.response.CustomerResponse;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

@UtilityClass
public class CustomerMapper {

  public static CustomerResponse toResponse(Customer data) {
    return CustomerResponse.builder()
        .idCustomer(data.getIdCliente())
        .nombre(data.getNombre())
        .apellido(data.getApellido())
        .dni(data.getDni())
        .email(data.getEmail())
        .telefono(data.getTelefono())
        .direccion(data.getDireccion())
        .fechaRegistro(DateUtils.dateTimeFormater(data.getFechaRegistro()))
        .estado(data.getEstado())
        .build();
  }

  public static Customer toEntity(CustomerRequest request) {
    return Customer.builder()
        .nombre(request.getNombre())
        .apellido(request.getApellido())
        .dni(request.getDni())
        .email(request.getEmail())
        .direccion(request.getDireccion())
        //.fechaRegistro(DateUtils.parseStringToDate(request.getFechaRegistro()))
        .telefono(request.getTelefono())
        .estado(request.getEstado())
        .build();
  }

  public static Customer toUpdateEntity(Customer data, CustomerRequest request) {
    Optional.ofNullable(request.getDni()).ifPresent(data::setDni);
    Optional.ofNullable(request.getEmail()).ifPresent(data::setEmail);
    Optional.ofNullable(request.getDireccion()).ifPresent(data::setDireccion);
    Optional.ofNullable(request.getTelefono()).ifPresent(data::setTelefono);
    Optional.ofNullable(request.getNombre()).ifPresent(data::setNombre);
    Optional.ofNullable(request.getApellido()).ifPresent(data::setApellido);
    Optional.ofNullable(request.getEstado()).ifPresent(data::setEstado);
    Optional.ofNullable(request.getUserId()).ifPresent(data::setUserId);
    Optional.ofNullable(request.getFechaRegistro()).ifPresent(date ->
        data.setFechaRegistro(DateUtils.parseStringToDate(date)));
    return data;
  }

  public static Payloads<List<CustomerResponse>> toListResponse(Page<Customer> content) {
    if (!content.hasContent()) {
      return new Payloads<>();
    }
    return toPayloads(content.map(CustomerMapper::toResponse));
  }

  private static Payloads<List<CustomerResponse>> toPayloads(Page<CustomerResponse> data) {
    Payloads<List<CustomerResponse>> response = new Payloads<>();
    response.setData(data.getContent());
    response.setTotalRegistros(data.getTotalElements());
    response.setPaginaActual(data.getPageable().getPageNumber());
    response.setTamanioPagina(data.getPageable().getPageSize());
    response.setTotalPaginas(data.getTotalPages());
    return response;
  }
}
