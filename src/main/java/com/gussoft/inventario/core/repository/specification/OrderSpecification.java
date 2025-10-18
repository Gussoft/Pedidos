package com.gussoft.inventario.core.repository.specification;

import com.gussoft.inventario.core.models.Order;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
@UtilityClass
public class OrderSpecification {

  public static Specification<Order> crearSpecificationVentasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (fechaInicio != null && fechaFin != null) {
        predicates.add(cb.between(root.get("fechaPedido"), fechaInicio, fechaFin));
      }

      // Incluir solo pedidos entregados o confirmados para reportes
      predicates.add(cb.or(
          cb.equal(root.get("estado"), "ENTREGADO"),
          cb.equal(root.get("estado"), "CONFIRMADO")
      ));

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
