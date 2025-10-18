package com.gussoft.inventario.core.models;

import com.gussoft.inventario.core.util.DateUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "producto")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_producto")
  private Long idProducto;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_categoria", nullable = false)
  private Category categoria;

  @Column(name = "nombre", nullable = false, length = 200)
  private String nombre;

  @Column(name = "descripcion", columnDefinition = "TEXT")
  private String descripcion;

  @Column(name = "precio", nullable = false, precision = 10, scale = 2)
  private BigDecimal precio;

  @Column(name = "stock")
  private Integer stock;

  @Column(name = "modelo", length = 100)
  private String modelo;

  @Column(name = "marca", length = 100)
  private String marca;

  @Column(name = "color", length = 50)
  private String color;

  @Column(name = "fecha_creacion")
  private LocalDateTime fechaCreacion;

  @Column(name = "estado")
  private Boolean estado;

  @PrePersist
  protected void onCreate() {
    fechaCreacion = DateUtils.fechaLima();
    if (estado == null) {
      estado = true;
    }
    if (stock == null) {
      stock = 0;
    }
  }
}
