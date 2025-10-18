package com.gussoft.inventario.core.business.impl;

import static com.gussoft.inventario.core.util.Constrains.FILTER_STATUS;
import static org.hibernate.binder.internal.TenantIdBinder.FILTER_NAME;

import com.gussoft.inventario.core.business.CategoryService;
import com.gussoft.inventario.core.exception.ResourceNotFoundException;
import com.gussoft.inventario.core.mappers.CategoryMapper;
import com.gussoft.inventario.core.models.Category;
import com.gussoft.inventario.core.models.common.Payloads;
import com.gussoft.inventario.core.repository.CategoryRepository;
import com.gussoft.inventario.core.repository.specification.CategorySpecification;
import com.gussoft.inventario.intregation.transfer.request.CategoryRequest;
import com.gussoft.inventario.intregation.transfer.response.CategoryResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import jakarta.transaction.Transactional;
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
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository repository;

  @Override
  public Payloads<List<CategoryResponse>> findAll(Integer numberPages, Integer numberRows, Map<String, String> filters) {
    if (numberPages <= 0) {
      numberPages = 1;
    }
    Page<Category> couponPage = getEntityListPage(filters, numberPages, numberRows);
    Payloads<List<CategoryResponse>> data = CategoryMapper.toListResponse(couponPage);
    if (data.getData() == null) {
      log.warn("No se encontraron categorías");
      return new Payloads<>();
    }
    return data;
  }

  @Override
  public CategoryResponse findById(Long id) {
    return CategoryMapper.toResponse(findEntityToId(id));
  }

  @Transactional
  @Override
  public CategoryResponse save(CategoryRequest request) {
    Category data = repository.save(CategoryMapper.toEntity(request));
    log.info("Categoría creada con ID: {}", data.getIdCategoria());
    return CategoryMapper.toResponse(data);
  }

  @Transactional
  @Override
  public CategoryResponse update(Long id, CategoryRequest request) {
    Category data = repository.save(CategoryMapper.toUpdateEntity(findEntityToId(id), request));
    log.info("Categoría actualizada con ID: {}", data.getIdCategoria());
    return CategoryMapper.toResponse(data);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    repository.delete(findEntityToId(id));
  }

  private Category findEntityToId(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> {
          String message = String.format("Categoría no encontrada con ID: %d", id);
          log.warn(message);
          return new ResourceNotFoundException(message);
        });
  }

  private Page<Category> getEntityListPage(Map<String, String> filters, Integer numberPages, Integer numberRows) {
    List<Specification<Category>> specificationList = new ArrayList<>();

    if (Objects.nonNull(filters)) {
      Optional.of(Objects.nonNull(filters.get(FILTER_NAME)))
          .filter(b -> b)
          .ifPresent(b ->
              specificationList.add(CategorySpecification.findByName(filters.get(FILTER_NAME)))
          );

      Optional.of(Objects.nonNull(filters.get(FILTER_STATUS)))
          .filter(b -> b)
          .ifPresent(b ->
              specificationList.add(CategorySpecification.findByStatus(Boolean.parseBoolean(filters.get(FILTER_STATUS))))
          );
    }

    Specification<Category> specification = CategorySpecification.findByCategoryId();
    for (Specification<Category> spec : specificationList) {
      specification = specification.and(spec);
    }

    Pageable pageable = PageRequest.of(numberPages - 1, numberRows,
        Sort.by("idCategoria").descending());
    return repository.findAll(specification, pageable);
  }
}
