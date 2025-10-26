package com.gussoft.inventario.core.repository;

import com.gussoft.inventario.core.models.Product;
import com.gussoft.inventario.intregation.transfer.record.IProductStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

  // Productos con stock bajo
  @Query("SELECT p.idProducto as idProducto, p.nombre as nombre, p.marca as marca, p.modelo as modelo, "
      + "p.stock as stock, p.precio as precio, c.nombre as categoria "
      + "FROM Product p JOIN p.categoria c "
      + "WHERE p.stock <= :stockMinimo AND p.estado = true "
      + "ORDER BY p.stock ASC")
  Page<IProductStock> findProductosStockBajo(@Param("stockMinimo") Integer stockMinimo, Pageable pageable);

  // Productos sin stock
  @Query("SELECT p.idProducto, p.nombre, p.marca, p.modelo, p.stock, p.precio " +
      "FROM Product p " +
      "WHERE p.stock = 0 AND p.estado = true")
  Page<IProductStock> findProductosSinStock(Pageable pageable);

  // Productos por categoría
  @Query("SELECT p.idProducto, p.nombre, p.precio, p.stock, p.marca " +
      "FROM Product p JOIN p.categoria c " +
      "WHERE c.idCategoria = :categoriaId AND p.estado = true")
  Page<Object[]> findProductosPorCategoria(@Param("categoriaId") Long categoriaId, Pageable pageable);

}
