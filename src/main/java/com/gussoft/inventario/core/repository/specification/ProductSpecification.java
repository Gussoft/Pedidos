package com.gussoft.inventario.core.repository.specification;

import static com.gussoft.inventario.core.util.Constrains.CRITERIA_LIKE_PERCENT;

import com.gussoft.inventario.core.models.Product;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
@UtilityClass
public class ProductSpecification {

  public static Specification<Product> findByProductId() {
    return (root, query, cb) ->
        cb.greaterThan(root.get("idProducto"), 0);
  }

  public static Specification<Product> findByName(String name) {
    return (root, query, cb) ->
        cb.like(root.get("nombre"), CRITERIA_LIKE_PERCENT + name + CRITERIA_LIKE_PERCENT);
  }

  public static Specification<Product> findByDescription(String name) {
    return (root, query, cb) ->
        cb.like(root.get("descripcion"), CRITERIA_LIKE_PERCENT + name + CRITERIA_LIKE_PERCENT);
  }

  public static Specification<Product> findByStock(String stock) {
    return (root, query, cb) ->
        cb.like(root.get("stock"), CRITERIA_LIKE_PERCENT + stock + CRITERIA_LIKE_PERCENT);
  }

  public static Specification<Product> findByStatus(boolean status) {
    return (root, query, cb) ->
        cb.equal(root.get("estado"), status);
  }
}
