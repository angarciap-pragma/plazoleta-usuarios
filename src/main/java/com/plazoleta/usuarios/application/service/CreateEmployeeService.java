package com.plazoleta.usuarios.application.service;

import com.plazoleta.usuarios.application.port.in.CreateEmployeeUseCase;
import com.plazoleta.usuarios.application.port.out.PasswordEncoderPort;
import com.plazoleta.usuarios.application.port.out.UsersRepositoryPort;
import com.plazoleta.usuarios.domain.exception.ConflictException;
import com.plazoleta.usuarios.domain.model.UsersModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CreateEmployeeService implements CreateEmployeeUseCase {

	private final UsersRepositoryPort usersRepository;
	private final PasswordEncoderPort passwordEncoder;

	private static final String EMAIL_EXIST = "El correo ya se encuentra registrado";
	private static final String DOCUMENT_NUMBER_EXIST = "El documento de identidad ya se encuentra registrado";

	@Override
	public UsersModel createEmployee(CreateEmployeeCommand command) {
		validateUniqueness(command.correo(), command.documentoIdentidad());
		log.info("Create employee start: documentId={}, email={}",
				command.documentoIdentidad(),
				command.correo());

		UsersModel usuario = UsersModel.createEmployee(
				command.nombre(),
				command.apellido(),
				command.documentoIdentidad(),
				command.celular(),
				command.correo(),
				passwordEncoder.encode(command.clave()),
				command.rol());

		UsersModel saved = usersRepository.save(usuario);
		log.info("Create employee saved: id={}", saved.id());
		return saved;
	}

	private void validateUniqueness(String email, String documentNumber) {
		if (usersRepository.existsByEmail(email)) {
			throw new ConflictException(EMAIL_EXIST);
		}
		if (usersRepository.existsByDocumentNumber(documentNumber)) {
			throw new ConflictException(DOCUMENT_NUMBER_EXIST);
		}
	}
}
