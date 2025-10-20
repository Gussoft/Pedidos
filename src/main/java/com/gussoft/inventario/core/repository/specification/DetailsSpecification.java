package com.gussoft.inventario.core.repository.specification;

import com.gussoft.inventario.core.models.Details;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
@UtilityClass
public class DetailsSpecification {

  public static Specification<Details> findByDetailsId() {
    return (root, query, cb) ->
        cb.greaterThan(root.get("idDetalle"), 0);
  }

  public static Specification<Details> findByCount(String can) {
    return (root, query, cb) ->
        cb.equal(root.get("cantidad"), can);
  }
}
