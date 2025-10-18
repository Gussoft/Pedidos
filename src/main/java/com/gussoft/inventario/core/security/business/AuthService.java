package com.gussoft.inventario.core.security.business;

import com.gussoft.inventario.core.exception.BusinessException;
import com.gussoft.inventario.core.mappers.CustomerMapper;
import com.gussoft.inventario.core.models.Customer;
import com.gussoft.inventario.core.repository.CustomerRepository;
import com.gussoft.inventario.core.security.entity.Role;
import com.gussoft.inventario.core.security.entity.User;
import com.gussoft.inventario.core.security.repository.UserRepository;
import com.gussoft.inventario.intregation.transfer.request.AuthRequest;
import com.gussoft.inventario.intregation.transfer.request.RegisterRequest;
import com.gussoft.inventario.intregation.transfer.response.AuthResponse;
import com.gussoft.inventario.intregation.transfer.response.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final CustomerRepository customerRepository;

  @Transactional
  public AuthResponse register(RegisterRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new BusinessException("El email ya está registrado");
    }

    var usuario = User.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole() != null ? request.getRole() : Role.USER)
        .build();

    userRepository.save(usuario);
    var customer = setCustomerRegistry(usuario.getId(), request);
    var jwtToken = jwtService.generateToken(usuario);

    return AuthResponse.builder()
        .token(jwtToken)
        .type("Bearer")
        .id(usuario.getId())
        .email(usuario.getEmail())
        .role(usuario.getRole())
        .cliente(customer)
        .build();
  }

  private CustomerResponse setCustomerRegistry(Long id, RegisterRequest request) {
    Customer customer = Customer.builder()
        .userId(id)
        .dni(request.getDni())
        .nombre(request.getNombre())
        .apellido(request.getApellido())
        .email(request.getEmail())
        .estado(true)
        .build();
    var data = customerRepository.save(customer);
    return CustomerMapper.toResponse(data);
  }

  public AuthResponse authenticate(AuthRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );

    var usuario = userRepository.findByEmail(request.getEmail())
        .orElseThrow();

    var jwtToken = jwtService.generateToken(usuario);

    return AuthResponse.builder()
        .token(jwtToken)
        .type("Bearer")
        .id(usuario.getId())
        .email(usuario.getEmail())
        .role(usuario.getRole())
        .build();
  }
}