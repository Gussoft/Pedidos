package com.gussoft.inventario.intregation.transfer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

  private Long idCustomer;

  private String dni;

  private String nombre;

  private String apellido;

  private String email;

  private String telefono;

  private String direccion;

  private String fechaRegistro;

  private Boolean estado;

}
