package com.gussoft.inventario.core.security.entity;

import com.gussoft.inventario.core.util.DateUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "El email es obligatorio")
  @Email(message = "El formato del email no es válido")
  @Column(unique = true, nullable = false, length = 100)
  private String email;

  @NotBlank(message = "La contraseña es obligatoria")
  @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Column(name = "fecha_creacion")
  private LocalDateTime fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private LocalDateTime fechaActualizacion;

  @Column(name = "cuenta_expirada")
  private boolean cuentaExpirada;

  @Column(name = "cuenta_bloqueada")
  private boolean cuentaBloqueada;

  @Column(name = "credenciales_expiradas")
  private boolean credencialesExpiradas;

  @Column(name = "habilitado")
  private boolean habilitado;


  @PrePersist
  protected void onCreate() {
    fechaCreacion = DateUtils.fechaLima();
    fechaActualizacion = DateUtils.fechaLima();
    habilitado = true;
  }

  @PreUpdate
  protected void onUpdate() {
    fechaActualizacion = DateUtils.fechaLima();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return !cuentaExpirada;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !cuentaBloqueada;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return !credencialesExpiradas;
  }

  @Override
  public boolean isEnabled() {
    return habilitado;
  }
}
