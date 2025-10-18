package com.gussoft.inventario.intregation.transfer.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class AuthRequest {

  @NotBlank(message = "El email es obligatorio")
  @Email(message = "El formato del email no es válido")
  private String email;

  @NotBlank(message = "La contraseña es obligatoria")
  private String password;

}