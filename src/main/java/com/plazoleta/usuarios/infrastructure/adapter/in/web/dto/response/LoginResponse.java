package com.plazoleta.usuarios.infrastructure.adapter.in.web.dto.response;

import com.plazoleta.usuarios.domain.model.Role;

public record LoginResponse(Long id, String correo, Role rol, String token) {

}
