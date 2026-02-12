package com.plazoleta.usuarios.application.port.out;

import com.plazoleta.usuarios.domain.model.UsersModel;
import java.util.Optional;

public interface UsersRepositoryPort {

	UsersModel save(UsersModel user);

	Optional<UsersModel> findById(Long id);

	Optional<UsersModel> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByDocumentNumber(String documentoIdentidad);
}
