package com.gussoft.inventario.intregation.transfer.response;

import com.gussoft.inventario.core.security.entity.Role;
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
public class AuthResponse {

  private String token;
  private String type;
  private Long id;
  private String email;

  private Role role;

  private CustomerResponse cliente;

}