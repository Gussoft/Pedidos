package com.gussoft.inventario.core.repository;

import com.gussoft.inventario.core.models.Order;
import com.gussoft.inventario.core.models.OrderStatus;
import com.gussoft.inventario.intregation.transfer.record.ClientesFrecuentes;
import com.gussoft.inventario.intregation.transfer.record.ClientesMoreBuy;
import com.gussoft.inventario.intregation.transfer.record.CustomerSould;
import com.gussoft.inventario.intregation.transfer.response.ReporteCliente;
import java.time.LocalDateTime;
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
  @Query("SELECT c.idCliente as idCliente, c.nombre as nombre, c.apellido as apellido, COUNT(o) as totalPedidos, SUM(o.total) as montoTotal "
      + "FROM Order o JOIN o.cliente c "
      + "WHERE o.fechaPedido BETWEEN :fechaInicio AND :fechaFin "
      + "AND o.estado = 'ENTREGADO' "
      + "GROUP BY c.idCliente, c.nombre, c.apellido")
  Page<ReporteCliente> findVentasPorCliente(@Param("fechaInicio") LocalDateTime fechaInicio,
                                            @Param("fechaFin") LocalDateTime fechaFin,
                                            Pageable pageable);

  // Clientes más frecuentes
  @Query("SELECT c.idCliente as idCliente, c.nombre as nombre, c.apellido as apellido, c.email as email, "
      + "COUNT(o.idPedido) as totalPedidos, "
      + "SUM(o.total) as totalGastado "
      + "FROM Order o JOIN o.cliente c "
      + "WHERE o.fechaPedido BETWEEN :fechaInicio AND :fechaFin "
      + "AND o.estado = 'ENTREGADO' "
      + "GROUP BY c.idCliente, c.nombre, c.apellido, c.email "
      + "ORDER BY totalPedidos DESC, totalGastado DESC")
  Page<ClientesFrecuentes> findClientesMasFrecuentes(@Param("fechaInicio") LocalDateTime fechaInicio,
                                                     @Param("fechaFin") LocalDateTime fechaFin,
                                                     Pageable pageable);

  // Clientes con mejor compra
  @Query("SELECT c.idCliente as idCliente, c.nombre as nombre, c.apellido as apellido, MAX(o.total) as mejorCompra " +
      "FROM Order o JOIN o.cliente c " +
      "WHERE o.fechaPedido BETWEEN :fechaInicio AND :fechaFin " +
      "AND o.estado = 'ENTREGADO' " +
      "GROUP BY c.idCliente, c.nombre, c.apellido " +
      "ORDER BY mejorCompra DESC")
  Page<ClientesMoreBuy> findClientesMejorCompra(@Param("fechaInicio") LocalDateTime fechaInicio,
                                                @Param("fechaFin") LocalDateTime fechaFin,
                                                Pageable pageable);

  @Query("SELECT c.idCliente as idCliente, c.nombre as nombre, c.apellido as apellido, c.email as email, "
      + "COUNT(o) as totalPedidos, SUM(o.total) as totalGastado "
      + "FROM Order o JOIN o.cliente c "
      + "WHERE o.fechaPedido BETWEEN :fechaInicio AND :fechaFin "
      + "AND o.estado = 'ENTREGADO' "
      + "GROUP BY c.idCliente, c.nombre, c.apellido, c.email "
      + "ORDER BY SUM(o.total) DESC")
  Page<CustomerSould> findClientesTop(@Param("fechaInicio") LocalDateTime fechaInicio,
                                      @Param("fechaFin") LocalDateTime fechaFin,
                                      Pageable pageable);

  @Query("SELECT o FROM Order o JOIN o.cliente c WHERE " +
      "c.dni = :dni " +
      "AND (:estado IS NULL OR o.estado = :estado) " +
      "AND (:fechaInicio IS NULL OR o.fechaPedido >= :fechaInicio) " +
      "AND (:fechaFin IS NULL OR o.fechaPedido <= :fechaFin)")
  Page<Order> findByClienteDniWithFilters(@Param("dni") String dni,
                                          @Param("estado") OrderStatus estado,
                                          @Param("fechaInicio") LocalDateTime fechaInicio,
                                          @Param("fechaFin") LocalDateTime fechaFin,
                                          Pageable pageable);

}
