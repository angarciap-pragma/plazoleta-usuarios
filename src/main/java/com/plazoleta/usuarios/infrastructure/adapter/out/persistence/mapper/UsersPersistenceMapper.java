package com.plazoleta.usuarios.infrastructure.adapter.out.persistence.mapper;

import com.plazoleta.usuarios.domain.model.UsersModel;
import com.plazoleta.usuarios.infrastructure.adapter.out.persistence.entity.UsersEntity;
import org.springframework.stereotype.Component;

@Component
public class UsersPersistenceMapper {

	public UsersEntity toEntity(UsersModel usuario) {
		return UsersEntity.builder()
				.id(usuario.id())
				.nombre(usuario.nombre())
				.apellido(usuario.apellido())
				.documentoIdentidad(usuario.documentoIdentidad())
				.celular(usuario.celular())
				.fechaNacimiento(usuario.fechaNacimiento())
				.correo(usuario.correo())
				.clave(usuario.clave())
				.rol(usuario.rol())
				.build();
	}

	public UsersModel toDomain(UsersEntity entity) {
		return new UsersModel(
				entity.getId(),
				entity.getNombre(),
				entity.getApellido(),
				entity.getDocumentoIdentidad(),
				entity.getCelular(),
				entity.getFechaNacimiento(),
				entity.getCorreo(),
				entity.getClave(),
				entity.getRol());
	}
}
