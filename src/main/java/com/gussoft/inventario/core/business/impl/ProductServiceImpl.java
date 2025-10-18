package com.gussoft.inventario.core.business.impl;

import static com.gussoft.inventario.core.util.Constrains.FILTER_DESCRIPTION;
import static com.gussoft.inventario.core.util.Constrains.FILTER_NAME;
import static com.gussoft.inventario.core.util.Constrains.FILTER_STATUS;
import static com.gussoft.inventario.core.util.Constrains.FILTER_STOCK;

import com.gussoft.inventario.core.business.ProductService;
import com.gussoft.inventario.core.exception.ResourceNotFoundException;
import com.gussoft.inventario.core.mappers.ProductMapper;
import com.gussoft.inventario.core.models.Category;
import com.gussoft.inventario.core.models.Product;
import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.core.repository.CategoryRepository;
import com.gussoft.inventario.core.repository.ProductRepository;
import com.gussoft.inventario.core.repository.specification.ProductSpecification;
import com.gussoft.inventario.intregation.transfer.request.ProductRequest;
import com.gussoft.inventario.intregation.transfer.response.ProductResponse;
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
public class ProductServiceImpl implements ProductService {

  private final ProductRepository repository;
  private final CategoryRepository categoryRepository;

  @Override
  public Payloads<List<ProductResponse>> findAll(Integer numberPages, Integer numberRows, Map<String, String> filters) {
    if (numberPages <= 0) {
      numberPages = 1;
    }
    Page<Product> couponPage = getEntityListPage(filters, numberPages, numberRows);
    Payloads<List<ProductResponse>> data = ProductMapper.toListResponse(couponPage);
    if (data.getData() == null) {
      log.warn("No se encontraron Product");
      return new Payloads<>();
    }
    return data;
  }

  @Override
  public ProductResponse findById(Long id) {
    return ProductMapper.toResponse(findEntityToId(id));
  }

  @Transactional
  @Override
  public ProductResponse save(ProductRequest request) {
    findCategoryToId(request.getCategoriaId());
    Product data = repository.save(ProductMapper.toEntity(request));
    log.info("Product creada con ID: {}", data.getIdProducto());
    return ProductMapper.toResponse(data);
  }

  @Transactional
  @Override
  public ProductResponse update(Long id, ProductRequest request) {
    Product data = repository.save(ProductMapper.toUpdateEntity(findEntityToId(id), request));
    log.info("Product actualizada con ID: {}", data.getIdProducto());
    return ProductMapper.toResponse(data);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    repository.delete(findEntityToId(id));
  }

  private Product findEntityToId(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> {
          String message = String.format("Product no encontrada con ID: %d", id);
          log.warn(message);
          return new ResourceNotFoundException(message);
        });
  }

  private Category findCategoryToId(Long id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> {
          String message = String.format("Category no encontrada con ID: %d", id);
          log.warn(message);
          return new ResourceNotFoundException(message);
        });
  }

  private Page<Product> getEntityListPage(Map<String, String> filters, Integer numberPages, Integer numberRows) {
    List<Specification<Product>> specificationList = new ArrayList<>();

    if (Objects.nonNull(filters)) {
      Optional.of(Objects.nonNull(filters.get(FILTER_NAME)))
          .filter(b -> b)
          .ifPresent(b ->
              specificationList.add(ProductSpecification.findByName(filters.get(FILTER_NAME)))
          );

      Optional.of(Objects.nonNull(filters.get(FILTER_DESCRIPTION)))
          .filter(b -> b)
          .ifPresent(b ->
              specificationList.add(ProductSpecification.findByDescription(filters.get(FILTER_DESCRIPTION)))
          );

      Optional.of(Objects.nonNull(filters.get(FILTER_STOCK)))
          .filter(b -> b)
          .ifPresent(b ->
              specificationList.add(ProductSpecification.findByStock(filters.get(FILTER_STOCK)))
          );

      Optional.of(Objects.nonNull(filters.get(FILTER_STATUS)))
          .filter(b -> b)
          .ifPresent(b ->
              specificationList.add(ProductSpecification.findByStatus(Boolean.parseBoolean(filters.get(FILTER_STATUS))))
          );
    }

    Specification<Product> specification = ProductSpecification.findByProductId();
    for (Specification<Product> spec : specificationList) {
      specification = specification.and(spec);
    }

    Pageable pageable = PageRequest.of(numberPages - 1, numberRows,
        Sort.by("idProducto").descending());
    return repository.findAll(specification, pageable);
  }
}
