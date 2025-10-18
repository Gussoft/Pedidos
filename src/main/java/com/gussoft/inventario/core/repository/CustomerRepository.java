package com.gussoft.inventario.core.repository;

import com.gussoft.inventario.core.models.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

  boolean existsByDni(String dni);

  boolean existsByEmail(String email);

  // Clientes con su total gastado
  @Query("SELECT c.idCliente, c.nombre, c.apellido, c.email, COUNT(o), COALESCE(SUM(o.total), 0) "
      + "FROM Customer c LEFT JOIN Order o ON o.cliente.idCliente = c.idCliente "
      + "AND o.fechaPedido BETWEEN :fechaInicio AND :fechaFin "
      + "AND o.estado = 'ENTREGADO' "
      + "GROUP BY c.idCliente, c.nombre, c.apellido, c.email "
      + "ORDER BY COALESCE(SUM(o.total), 0) DESC")
  Page<Object[]> findClientesConTotalGastado(@Param("fechaInicio") LocalDateTime fechaInicio,
                                             @Param("fechaFin") LocalDateTime fechaFin,
                                             Pageable pageable);

}
