package com.gussoft.inventario.intregation.transfer.request;

import jakarta.validation.constraints.Email;
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
public class CustomerRequest {

  private Long clienteId;

  @NotNull(message = "El dni es obligatorio")
  @NotBlank(message = "El dni no puede estar vacio")
  @Size(min = 8, max = 8, message = "El dni debe tener 8 caracteres.")
  private String dni;

  @NotNull(message = "El nombre es obligatorio")
  @NotBlank(message = "El nombre no puede estar vacio")
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
  private String nombre;

  @NotNull(message = "El apellido es obligatorio")
  @NotBlank(message = "El apellido no puede estar vacio")
  @Size(min = 3, max = 100, message = "El apellido debe tener entre 3 y 100 caracteres.")
  private String apellido;

  @Email(message = "El email debe ser valido")
  @NotBlank(message = "El email no puede estar vacio")
  private String email;

  @NotNull(message = "El telefono es obligatorio")
  @NotBlank(message = "El telefono no puede estar vacio")
  @Size(min = 9, max = 12, message = "El telefono debe tener entre 9 a 15 caracteres.")
  private String telefono;

  @NotNull(message = "La direccion es obligatoria")
  @NotBlank(message = "La direccion no puede estar vacia")
  @Size(min = 5, max = 150, message = "La direccion debe tener entre 5 y 150 caracteres.")
  private String direccion;

  private String fechaRegistro;

  private Long userId;

  private Boolean estado;

}
