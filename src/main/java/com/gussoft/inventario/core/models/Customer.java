package com.gussoft.inventario.core.models;

import com.gussoft.inventario.core.util.DateUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_cliente")
  private Long idCliente;

  @Column(name = "dni", nullable = false, length = 8, unique = true)
  private String dni;

  @Column(name = "nombre", nullable = false, length = 100)
  private String nombre;

  @Column(name = "apellido", nullable = false, length = 100)
  private String apellido;

  @Column(name = "email", nullable = false, unique = true, length = 150)
  private String email;

  @Column(name = "telefono", length = 20)
  private String telefono;

  @Column(name = "direccion", columnDefinition = "TEXT")
  private String direccion;

  @Column(name = "fecha_registro")
  private LocalDateTime fechaRegistro;

  @Column(name = "estado")
  private Boolean estado;

  @Column(name = "user_id")
  private Long userId;

  @PrePersist
  protected void onCreate() {
    fechaRegistro = DateUtils.fechaLima();
    if (estado == null) {
      estado = true;
    }
  }
}