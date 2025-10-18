package com.gussoft.inventario.core.models;

import com.gussoft.inventario.core.util.DateUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pedido")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_pedido")
  private Long idPedido;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_cliente", nullable = false)
  private Customer cliente;

  @Column(name = "fecha_pedido")
  private LocalDateTime fechaPedido;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado", length = 50)
  private OrderStatus estado;

  @Column(name = "total", precision = 10, scale = 2)
  private BigDecimal total;

  @Column(name = "direccion_envio", columnDefinition = "TEXT")
  private String direccionEnvio;

  @Column(name = "metodo_pago", length = 50)
  private String metodoPago;

  @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Details> detalles;

  @PrePersist
  protected void onCreate() {
    fechaPedido = DateUtils.fechaLima();
    if (estado == null) {
      estado = OrderStatus.PENDIENTE;
    }
    if (total == null) {
      total = BigDecimal.ZERO;
    }
  }

  public void changeEstado(OrderStatus nuevoEstado) {
    if (!this.estado.canTransitionTo(nuevoEstado)) {
      throw new IllegalStateException(
          String.format("No se puede cambiar de %s a %s", this.estado, nuevoEstado));
    }
    this.estado = nuevoEstado;
  }
}