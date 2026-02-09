package com.plazoleta.usuarios.application.port.out;

import com.plazoleta.usuarios.domain.model.Usuario;
import java.util.Optional;

public interface UsuarioRepositoryPort {

	Usuario save(Usuario usuario);

	Optional<Usuario> findById(Long id);

	Optional<Usuario> findByCorreo(String correo);
}
