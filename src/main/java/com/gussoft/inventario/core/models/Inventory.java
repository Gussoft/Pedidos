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
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_inventario")
  private Long idInventario;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_producto", nullable = false)
  private Product producto;

  @Column(name = "cantidad")
  private Integer cantidad;

  @Column(name = "tipo_movimiento", length = 50)
  private String tipoMovimiento;

  @Column(name = "fecha_movimiento")
  private LocalDateTime fechaMovimiento;

  @Column(name = "descripcion", length = 500)
  private String descripcion;

  @PrePersist
  protected void onCreate() {
    fechaMovimiento = DateUtils.fechaLima();
  }
}