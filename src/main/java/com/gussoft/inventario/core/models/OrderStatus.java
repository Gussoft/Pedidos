package com.gussoft.inventario.core.models;

public enum OrderStatus {

  PENDIENTE("Pendiente"),
  ENTREGADO("Entregado"),
  CANCELADO("Cancelado");

  private final String descripcion;

  OrderStatus(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public boolean canTransitionTo(OrderStatus nuevoEstado) {
    return switch (this) {
      case PENDIENTE -> nuevoEstado == ENTREGADO || nuevoEstado == CANCELADO;
      case ENTREGADO, CANCELADO -> false;
    };
  }
}
