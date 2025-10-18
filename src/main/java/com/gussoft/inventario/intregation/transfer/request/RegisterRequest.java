package com.gussoft.inventario.intregation.transfer.request;

import com.gussoft.inventario.core.security.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class RegisterRequest {

  @NotBlank(message = "El dni es obligatorio")
  @Size(min = 8, max = 8, message = "El dni debe tener de 8 caracteres")
  private String dni;

  @NotBlank(message = "El nombre es obligatorio")
  @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
  private String nombre;

  @NotBlank(message = "El apellido es obligatorio")
  @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
  private String apellido;

  @NotBlank(message = "El email es obligatorio")
  @Email(message = "El formato del email no es válido")
  private String email;

  @NotBlank(message = "La contraseña es obligatoria")
  @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
  private String password;

  private Role role;

}