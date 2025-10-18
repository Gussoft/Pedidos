package com.gussoft.inventario.intregation.transfer.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

  private Long categoriaId;

  @NotNull(message = "El nombre es obligatorio")
  @NotBlank(message = "El nombre no puede estar vacio")
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
  private String nombre;

  private String descripcion;

  private Boolean estado;

}
