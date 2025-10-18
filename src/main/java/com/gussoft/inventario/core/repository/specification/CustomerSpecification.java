package com.gussoft.inventario.core.repository.specification;

import static com.gussoft.inventario.core.util.Constrains.CRITERIA_LIKE_PERCENT;

import com.gussoft.inventario.core.models.Customer;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
@UtilityClass
public class CustomerSpecification {

  public static Specification<Customer> findByCustomerId() {
    return (root, query, cb) ->
        cb.greaterThan(root.get("idCliente"), 0);
  }

  public static Specification<Customer> findByName(String name) {
    return (root, query, cb) ->
        cb.like(root.get("nombre"), CRITERIA_LIKE_PERCENT + name + CRITERIA_LIKE_PERCENT);
  }

  public static Specification<Customer> findByLastName(String name) {
    return (root, query, cb) ->
        cb.like(root.get("apellido"), CRITERIA_LIKE_PERCENT + name + CRITERIA_LIKE_PERCENT);
  }

  public static Specification<Customer> findByPhone(String name) {
    return (root, query, cb) ->
        cb.like(root.get("telefono"), CRITERIA_LIKE_PERCENT + name + CRITERIA_LIKE_PERCENT);
  }

  public static Specification<Customer> findByDni(String dni) {
    return (root, query, cb) ->
        cb.like(root.get("dni"), CRITERIA_LIKE_PERCENT + dni + CRITERIA_LIKE_PERCENT);
  }

  public static Specification<Customer> findByEmail(String name) {
    return (root, query, cb) ->
        cb.like(root.get("email"), CRITERIA_LIKE_PERCENT + name + CRITERIA_LIKE_PERCENT);
  }

  public static Specification<Customer> findByStatus(boolean status) {
    return (root, query, cb) ->
        cb.equal(root.get("estado"), status);
  }
}
