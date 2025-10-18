package com.gussoft.inventario.intregation.transfer.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

  private Long idCategoria;

  private String nombre;

  private String descripcion;

  private String fechaCreacion;

  private Boolean activo;

}
