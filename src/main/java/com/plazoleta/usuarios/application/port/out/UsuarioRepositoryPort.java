package com.plazoleta.usuarios.application.port.out;

import com.plazoleta.usuarios.domain.model.Usuario;

public interface UsuarioRepositoryPort {

	Usuario save(Usuario usuario);
}
