package com.plazoleta.usuarios.application.service;

import com.plazoleta.usuarios.application.port.in.CreatePropietarioUseCase;
import com.plazoleta.usuarios.application.port.out.PasswordEncoderPort;
import com.plazoleta.usuarios.application.port.out.UsuarioRepositoryPort;
import com.plazoleta.usuarios.domain.model.Role;
import com.plazoleta.usuarios.domain.model.Usuario;
import java.time.LocalDate;
import java.time.Period;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CrearPropietarioService implements CreatePropietarioUseCase {

	private static final int MAYORIA_DE_EDAD = 18;

	private final UsuarioRepositoryPort usuarioRepository;
	private final PasswordEncoderPort passwordEncoder;

	public CrearPropietarioService(
			UsuarioRepositoryPort usuarioRepository,
			PasswordEncoderPort passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Usuario crearPropietario(CreatePropietarioCommand command) {
		if (!esMayorDeEdad(command.fechaNacimiento())) {
			log.warn("Create propietario rejected: underage documentoIdentidad={}", command.documentoIdentidad());
			throw new IllegalArgumentException("El usuario debe ser mayor de edad");
		}

		log.info("Create propietario start: documentoIdentidad={}, correo={}",
				command.documentoIdentidad(),
				command.correo());
		Usuario usuario = new Usuario(
				null,
				command.nombre(),
				command.apellido(),
				command.documentoIdentidad(),
				command.celular(),
				command.fechaNacimiento(),
				command.correo(),
				passwordEncoder.encode(command.clave()),
				Role.PROPIETARIO);

		Usuario saved = usuarioRepository.save(usuario);
		log.info("Create propietario saved: id={}", saved.getId());
		return saved;
	}

	private boolean esMayorDeEdad(LocalDate fechaNacimiento) {
		LocalDate hoy = LocalDate.now();
		return Period.between(fechaNacimiento, hoy).getYears() >= MAYORIA_DE_EDAD;
	}
}
