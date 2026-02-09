package com.plazoleta.usuarios.adapters.out.persistence;

import com.plazoleta.usuarios.application.port.out.UsuarioRepositoryPort;
import com.plazoleta.usuarios.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {

	private final UsuarioJpaRepository usuarioJpaRepository;

	public UsuarioPersistenceAdapter(UsuarioJpaRepository usuarioJpaRepository) {
		this.usuarioJpaRepository = usuarioJpaRepository;
	}

	@Override
	public Usuario save(Usuario usuario) {
		UsuarioEntity entity = new UsuarioEntity(
				usuario.getId(),
				usuario.getNombre(),
				usuario.getApellido(),
				usuario.getDocumentoIdentidad(),
				usuario.getCelular(),
				usuario.getFechaNacimiento(),
				usuario.getCorreo(),
				usuario.getClave(),
				usuario.getRol());
		UsuarioEntity saved = usuarioJpaRepository.save(entity);
		return new Usuario(
				saved.getId(),
				saved.getNombre(),
				saved.getApellido(),
				saved.getDocumentoIdentidad(),
				saved.getCelular(),
				saved.getFechaNacimiento(),
				saved.getCorreo(),
				saved.getClave(),
				saved.getRol());
	}
}
