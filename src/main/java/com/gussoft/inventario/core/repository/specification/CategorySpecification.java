package com.gussoft.inventario.core.repository.specification;

import static com.gussoft.inventario.core.util.Constrains.CRITERIA_LIKE_PERCENT;

import com.gussoft.inventario.core.models.Category;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
@UtilityClass
public class CategorySpecification {

  public static Specification<Category> findByCategoryId() {
    return (root, query, cb) ->
        cb.greaterThan(root.get("idCategoria"), 0);
  }

  public static Specification<Category> findByName(String name) {
    return (root, query, cb) ->
        cb.like(root.get("nombre"), CRITERIA_LIKE_PERCENT + name + CRITERIA_LIKE_PERCENT);
  }

  public static Specification<Category> findByStatus(boolean status) {
    return (root, query, cb) ->
        cb.equal(root.get("estado"), status);
  }
}
