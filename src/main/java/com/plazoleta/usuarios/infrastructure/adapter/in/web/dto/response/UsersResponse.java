package com.plazoleta.usuarios.infrastructure.adapter.in.web.dto.response;

import com.plazoleta.usuarios.domain.model.Role;

public record UsersResponse(Long id, String nombre, String apellido, String documentoIdentidad, String celular,
                            String correo, Role rol) {

}
