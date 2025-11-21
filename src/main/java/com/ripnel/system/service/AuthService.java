package com.ripnel.system.service;

import com.ripnel.system.model.User;
import com.ripnel.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User login(String email, String password) {

        System.out.println(">>> email recibido = '" + email + "'");
        System.out.println(">>> password recibido = '" + password + "'");

        Optional<User> opt = userRepository.findByEmail(email);

        if (opt.isEmpty()) {
            System.out.println(">>> Usuario no encontrado");
            return null;
        }

        User user = opt.get();

        System.out.println(">>> password en BD = '" + user.getPasswordHash() + "'");

        if (user.getActive() == null || !user.getActive()) {
            System.out.println(">>> Usuario inactivo");
            return null;
        }

        if (!password.equals(user.getPasswordHash())) {
            System.out.println(">>> PASSWORD NO COINCIDE");
            return null;
        }

        System.out.println(">>> LOGIN OK !!!!");
        return user;
    }
}
