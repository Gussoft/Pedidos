package com.gussoft.inventario.core.security.business;

import com.gussoft.inventario.core.exception.ResourceNotFoundException;
import com.gussoft.inventario.core.models.OrderStatus;
import com.gussoft.inventario.core.repository.DetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("detailSecurity")
@RequiredArgsConstructor
public class DetailsSecurity {

  private final DetailsRepository detailsRepository;

  public boolean canDeleteDetail(Authentication authentication, Long idDetalle) {
    var detail = detailsRepository.findById(idDetalle).orElse(null);
    if (detail == null) {
      throw new ResourceNotFoundException("Detail no encontrado");
    }

    boolean isAdmin = authentication.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    boolean entregado = detail.getPedido().getEstado() == OrderStatus.ENTREGADO;
    if (entregado && !isAdmin) {
      return false;
    }
    return true;
  }

}
