package com.plazoleta.usuarios.application.service;

import com.plazoleta.usuarios.application.port.in.CreateOwnerUseCase;
import com.plazoleta.usuarios.application.port.out.PasswordEncoderPort;
import com.plazoleta.usuarios.application.port.out.UsersRepositoryPort;
import com.plazoleta.usuarios.domain.exception.ConflictException;
import com.plazoleta.usuarios.domain.model.UsersModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CreateOwnerService implements CreateOwnerUseCase {

	private final UsersRepositoryPort usuarioRepository;
	private final PasswordEncoderPort passwordEncoder;

	private static final String EMAIL_EXIST = "El correo ya se encuentra registrado";
	private static final String DOCUMENT_NUMBER_EXIST = "El documento de identidad ya se encuentra registrado";

	@Override
	public UsersModel createOwner(CreatePropietarioCommand command) {
		validarUnicidad(command.correo(), command.documentoIdentidad());
		log.info("Create owner start: documentId={}, email={}",
				command.documentoIdentidad(),
				command.correo());
		UsersModel usuario = UsersModel.createOwner(
				command.nombre(),
				command.apellido(),
				command.documentoIdentidad(),
				command.celular(),
				command.fechaNacimiento(),
				command.correo(),
				passwordEncoder.encode(command.clave()));

		UsersModel saved = usuarioRepository.save(usuario);
		log.info("Create owner saved: id={}", saved.id());
		return saved;
	}

	private void validarUnicidad(String correo, String documentoIdentidad) {
		if (usuarioRepository.existsByEmail(correo)) {
			throw new ConflictException(EMAIL_EXIST);
		}
		if (usuarioRepository.existsByDocumentNumber(documentoIdentidad)) {
			throw new ConflictException(DOCUMENT_NUMBER_EXIST);
		}
	}

}
