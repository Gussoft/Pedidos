package com.gussoft.inventario.core.repository;

import com.gussoft.inventario.core.models.Details;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.gussoft.inventario.intregation.transfer.record.ProductStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailsRepository extends JpaRepository<Details, Long>, JpaSpecificationExecutor<Details> {

  // Ventas por producto con información completa
  @Query("SELECT p.idProducto, p.nombre, p.marca, p.modelo, SUM(d.cantidad) as totalVendido, SUM(d.subtotal) as totalVentas " +
      "FROM Details d JOIN d.producto p JOIN d.pedido o " +
      "WHERE o.fechaPedido BETWEEN :fechaInicio AND :fechaFin " +
      "AND o.estado = 'ENTREGADO' " +
      "GROUP BY p.idProducto, p.nombre, p.marca, p.modelo " +
      "ORDER BY totalVendido DESC")
  Page<Object[]> findVentasPorProducto(@Param("fechaInicio") LocalDateTime fechaInicio,
                                       @Param("fechaFin") LocalDateTime fechaFin,
                                       Pageable pageable);

  // Ventas por categoría
  @Query("SELECT c.idCategoria, c.nombre, SUM(d.cantidad) as totalUnidades, SUM(d.subtotal) as totalVentas " +
      "FROM Details d JOIN d.producto p JOIN p.categoria c JOIN d.pedido o " +
      "WHERE o.fechaPedido BETWEEN :fechaInicio AND :fechaFin " +
      "AND o.estado = 'ENTREGADO' " +
      "GROUP BY c.idCategoria, c.nombre " +
      "ORDER BY totalVentas DESC")
  Page<ProductStock> findVentasPorCategoria(@Param("fechaInicio") LocalDateTime fechaInicio,
                                        @Param("fechaFin") LocalDateTime fechaFin,
                                        Pageable pageable);

  // Productos más vendidos
  @Query("SELECT p.idProducto, p.nombre, p.marca, p.modelo, SUM(d.cantidad) as totalVendido " +
      "FROM Details d JOIN d.producto p JOIN d.pedido o " +
      "WHERE o.fechaPedido BETWEEN :fechaInicio AND :fechaFin " +
      "AND o.estado = 'ENTREGADO' " +
      "GROUP BY p.idProducto, p.nombre, p.marca, p.modelo " +
      "ORDER BY totalVendido DESC")
  Page<ProductStock> findProductosMasVendidos(@Param("fechaInicio") LocalDateTime fechaInicio,
                                              @Param("fechaFin") LocalDateTime fechaFin,
                                              Pageable pageable);

}
