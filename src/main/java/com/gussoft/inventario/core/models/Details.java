package com.gussoft.inventario.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalle_pedido")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Details {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_detalle")
  private Long idDetalle;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_pedido", nullable = false)
  private Order pedido;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_producto", nullable = false)
  private Product producto;

  @Column(name = "cantidad")
  private Integer cantidad;

  @Column(name = "precio_unitario", precision = 10, scale = 2)
  private BigDecimal precioUnitario;

  @Column(name = "subtotal", precision = 10, scale = 2)
  private BigDecimal subtotal;

  @PrePersist
  @PreUpdate
  public void calcularSubtotal() {
    if (precioUnitario != null && cantidad != null) {
      subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
  }
}