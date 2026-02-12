package com.plazoleta.usuarios.testutil;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptGenTool {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: BcryptGenTool <password>");
			System.exit(1);
		}
		String hash = new BCryptPasswordEncoder().encode(args[0]);
		System.out.println(hash);
	}
}
