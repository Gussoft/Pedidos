package com.gussoft.inventario.core.models.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Payloads<T> {

  @Valid
  @JsonProperty("datos")
  private T data;

  @JsonProperty("totalRegistros")
  private Long totalRegistros;

  @JsonProperty("totalPaginas")
  private Integer totalPaginas;

  @JsonProperty("paginaActual")
  private Integer paginaActual;

  @JsonProperty("tamanioPagina")
  private Integer tamanioPagina;

}
