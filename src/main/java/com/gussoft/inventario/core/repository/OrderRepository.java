package com.gussoft.inventario.core.repository;

import com.gussoft.inventario.core.models.Order;
import java.time.LocalDateTime;
import com.gussoft.inventario.intregation.transfer.record.CustomerSould;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

  // Ventas por cliente con información completa
  @Query("SELECT c.idCliente, c.nombre, c.apellido, COUNT(o), SUM(o.total) " +
      "FROM Order o JOIN o.cliente c " +
      "WHERE o.fechaPedido BETWEEN :fechaInicio AND :fechaFin " +
      "AND o.estado = 'ENTREGADO' " +
      "GROUP BY c.idCliente, c.nombre, c.apellido")
  Page<Object[]> findVentasPorCliente(@Param("fechaInicio") LocalDateTime fechaInicio,
                                      @Param("fechaFin") LocalDateTime fechaFin,
                                      Pageable pageable);

  // Clientes más frecuentes
  @Query("SELECT c.idCliente, c.nombre, c.apellido, c.email, "
      + "COUNT(o.idPedido) as totalPedidos, "
      + "SUM(o.total) as totalGastado "
      + "FROM Order o JOIN o.cliente c "
      + "WHERE o.fechaPedido BETWEEN :fechaInicio AND :fechaFin "
      + "AND o.estado = 'ENTREGADO' "
      + "GROUP BY c.idCliente, c.nombre, c.apellido, c.email "
      + "ORDER BY totalPedidos DESC, totalGastado DESC")
  Page<Object[]> findClientesMasFrecuentes(@Param("fechaInicio") LocalDateTime fechaInicio,
                                           @Param("fechaFin") LocalDateTime fechaFin,
                                           Pageable pageable);

  // Clientes con mejor compra
  @Query("SELECT c.idCliente, c.nombre, c.apellido, MAX(o.total) as mejorCompra " +
      "FROM Order o JOIN o.cliente c " +
      "WHERE o.fechaPedido BETWEEN :fechaInicio AND :fechaFin " +
      "AND o.estado = 'ENTREGADO' " +
      "GROUP BY c.idCliente, c.nombre, c.apellido " +
      "ORDER BY mejorCompra DESC")
  Page<Object[]> findClientesMejorCompra(@Param("fechaInicio") LocalDateTime fechaInicio,
                                         @Param("fechaFin") LocalDateTime fechaFin,
                                         Pageable pageable);

  @Query("SELECT new com.gussoft.inventario.intregation.transfer.record.CustomerSould(" +
      "c.idCliente, c.nombre, c.apellido, c.email, COUNT(o), SUM(o.total)) " +
      "FROM Order o JOIN o.cliente c " +
      "WHERE o.fechaPedido BETWEEN :fechaInicio AND :fechaFin " +
      "AND o.estado = 'ENTREGADO' " +
      "GROUP BY c.idCliente, c.nombre, c.apellido, c.email " +
      "ORDER BY SUM(o.total) DESC")
  Page<CustomerSould> findClientesTop(@Param("fechaInicio") LocalDateTime fechaInicio,
                                      @Param("fechaFin") LocalDateTime fechaFin,
                                      Pageable pageable);
}
