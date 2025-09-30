package com.ripnel.system;

import com.ripnel.system.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RipnelSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RipnelSystemApplication.class, args);
	}

	// Esto corre al iniciar. Si la conexión falla, aquí explota y verás el error.
	@org.springframework.context.annotation.Bean
	CommandLineRunner init(UserRepository userRepo) {
		return args -> {
			long total = userRepo.count();
			System.out.println(">>> BD OK. Usuarios en tabla 'users': " + total);
		};
	}
}