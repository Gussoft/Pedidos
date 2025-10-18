package com.gussoft.inventario.intregation.transfer.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

  private Long productoId;

  @NotNull(message = "La categoría es obligatoria")
  @Positive(message = "El ID de categoría debe ser un número positivo")
  private Long categoriaId;

  @NotNull(message = "El nombre es obligatorio")
  @NotBlank(message = "El nombre no puede estar vacio")
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
  private String nombre;

  @NotNull(message = "El descripcion es obligatorio")
  @NotBlank(message = "El descripcion no puede estar vacio")
  @Size(min = 3, max = 150, message = "El descripcion debe tener entre 3 y 150 caracteres.")
  private String descripcion;

  @NotNull(message = "El precio es obligatorio")
  @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
  @Digits(integer = 10, fraction = 2, message = "El precio debe tener máximo 10 enteros y 2 decimales")
  private BigDecimal precio;

  @NotNull(message = "El stock es obligatorio")
  @PositiveOrZero(message = "El stock debe ser un numero positivo o cero")
  private Integer stock;

  private String modelo;

  private String marca;

  private String color;

  private String fechaCreacion;

  private Boolean estado;

}
