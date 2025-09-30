package com.ripnel.system.service;

import com.ripnel.system.model.User;
import com.ripnel.system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepo;
    public AuthService(UserRepository userRepo) { this.userRepo = userRepo; }

    // Para el Sprint 1 usamos password en texto plano (luego pasamos a BCrypt)
    public Optional<User> login(String email, String rawPassword) {
        var found = userRepo.findByEmailAndActiveTrue(email);
        System.out.println("LOGIN email=" + email + " existe=" + found.isPresent());
        found.ifPresent(u -> System.out.println("PW_BD=" + u.getPasswordHash() + " PW_IN=" + rawPassword));
        return found.filter(u -> Objects.equals(u.getPasswordHash(), rawPassword));
    }
}
