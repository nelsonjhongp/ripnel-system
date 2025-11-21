package com.ripnel.system.controller;

import com.ripnel.system.model.User;
import com.ripnel.system.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session) {

        System.out.println(">>> Intento de login: " + email);

        User user = authService.login(email, password);

        if (user == null) {
            System.out.println(">>> Login falló: usuario null");
            return "redirect:/login?error";
        }

        System.out.println(">>> Login OK, usuario: " + user.getEmail());

        // ✔ CORRECTO: guardar como USER
        session.setAttribute("USER", user);

        // Redirección por rol
        if (user.hasRole("ADMIN"))
            return "redirect:/admin";

        if (user.hasRole("COMPRAS"))
            return "redirect:/compras/ordenes";

        if (user.hasRole("ALMACEN"))
            return "redirect:/almacen/movimientos";

        if (user.hasRole("PRODUCCION"))
            return "redirect:/produccion/ordenes";

        if (user.hasRole("VENTAS") || user.hasRole("VENDEDOR"))
            return "redirect:/ventas";

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }
}
