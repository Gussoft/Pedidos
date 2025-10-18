package com.gussoft.inventario.core.business.impl;

import static com.gussoft.inventario.core.util.Constrains.FILTER_DNI;
import static com.gussoft.inventario.core.util.Constrains.FILTER_EMAIL;
import static com.gussoft.inventario.core.util.Constrains.FILTER_LASTNAME;
import static com.gussoft.inventario.core.util.Constrains.FILTER_NAME;
import static com.gussoft.inventario.core.util.Constrains.FILTER_PHONE;
import static com.gussoft.inventario.core.util.Constrains.FILTER_STATUS;

import com.gussoft.inventario.core.business.CustomerService;
import com.gussoft.inventario.core.exception.BusinessException;
import com.gussoft.inventario.core.exception.ResourceNotFoundException;
import com.gussoft.inventario.core.mappers.CustomerMapper;
import com.gussoft.inventario.core.models.Customer;
import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.core.repository.CustomerRepository;
import com.gussoft.inventario.core.repository.specification.CustomerSpecification;
import com.gussoft.inventario.intregation.transfer.request.CustomerRequest;
import com.gussoft.inventario.intregation.transfer.response.CustomerResponse;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository repository;

  @Override
  public Payloads<List<CustomerResponse>> findAll(Integer numberPages, Integer numberRows, Map<String, String> filters) {
    if (numberPages <= 0) {
      numberPages = 1;
    }
    Page<Customer> couponPage = getEntityListPage(filters, numberPages, numberRows);
    Payloads<List<CustomerResponse>> data = CustomerMapper.toListResponse(couponPage);
    if (data.getData() == null) {
      log.warn("No se encontraron Customer");
      return new Payloads<>();
    }
    return data;
  }

  @Override
  public CustomerResponse findById(Long id) {
    return CustomerMapper.toResponse(findEntityToId(id));
  }

  @Transactional
  @Override
  public CustomerResponse save(CustomerRequest request) {
    validateFieldDuplicated(request);
    Customer data = repository.save(CustomerMapper.toEntity(request));
    log.info("Customer creada con ID: {}", data.getIdCliente());
    return CustomerMapper.toResponse(data);
  }

  @Transactional
  @Override
  public CustomerResponse update(Long id, CustomerRequest request) {
    Customer data = repository.save(CustomerMapper.toUpdateEntity(findEntityToId(id), request));
    log.info("Customer actualizada con ID: {}", data.getIdCliente());
    return CustomerMapper.toResponse(data);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    repository.delete(findEntityToId(id));
  }

  public Customer findEntityToId(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> {
          String message = String.format("Customer no encontrado con ID: %d", id);
          log.warn(message);
          return new ResourceNotFoundException(message);
        });
  }

  private Page<Customer> getEntityListPage(Map<String, String> filters, Integer numberPages, Integer numberRows) {
    List<Specification<Customer>> specificationList = new ArrayList<>();

    if (Objects.nonNull(filters)) {
      Optional.of(Objects.nonNull(filters.get(FILTER_NAME)))
          .filter(b -> b)
          .ifPresent(b ->
              specificationList.add(CustomerSpecification.findByName(filters.get(FILTER_NAME)))
          );

      Optional.of(Objects.nonNull(filters.get(FILTER_LASTNAME)))
          .filter(b -> b)
          .ifPresent(b ->
              specificationList.add(CustomerSpecification.findByLastName(filters.get(FILTER_LASTNAME)))
          );

      Optional.of(Objects.nonNull(filters.get(FILTER_DNI)))
          .filter(b -> b)
          .ifPresent(b ->
              specificationList.add(CustomerSpecification.findByDni(filters.get(FILTER_DNI)))
          );

      Optional.of(Objects.nonNull(filters.get(FILTER_PHONE)))
          .filter(b -> b)
          .ifPresent(b ->
              specificationList.add(CustomerSpecification.findByPhone(filters.get(FILTER_PHONE)))
          );

      Optional.of(Objects.nonNull(filters.get(FILTER_EMAIL)))
          .filter(b -> b)
          .ifPresent(b ->
              specificationList.add(CustomerSpecification.findByEmail(filters.get(FILTER_EMAIL)))
          );

      Optional.of(Objects.nonNull(filters.get(FILTER_STATUS)))
          .filter(b -> b)
          .ifPresent(b ->
              specificationList.add(CustomerSpecification.findByStatus(Boolean.parseBoolean(filters.get(FILTER_STATUS))))
          );
    }

    Specification<Customer> specification = CustomerSpecification.findByCustomerId();
    for (Specification<Customer> spec : specificationList) {
      specification = specification.and(spec);
    }

    Pageable pageable = PageRequest.of(numberPages - 1, numberRows,
        Sort.by("idCliente").descending());
    return repository.findAll(specification, pageable);
  }

  private void validateFieldDuplicated(CustomerRequest request) {
    if (repository.existsByDni(request.getDni())) {
      String message = String.format("Ya existe ese DNI");
      log.warn(message);
      throw new BusinessException(message);
    }
    if (repository.existsByEmail(request.getEmail())) {
      String message = String.format("Ya existe un Email Registrado!");
      log.warn(message);
      throw new BusinessException(message);
    }
  }
}
