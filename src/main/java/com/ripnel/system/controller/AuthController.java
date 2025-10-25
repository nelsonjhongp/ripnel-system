package com.ripnel.system.controller;

import com.ripnel.system.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {
        return authService.login(email, password)
                .map(u -> {
                    session.setAttribute("USER", u);
                    boolean isAdmin = u.getRoles()!=null && u.getRoles().stream()
                            .anyMatch(r -> "ADMIN".equalsIgnoreCase(r.getName()));
                    return isAdmin ? "redirect:/admin" : "redirect:/home";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Credenciales inválidas");
                    return "login";
                });
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/")
    public String root(HttpSession session){
        return (session.getAttribute("USER") != null)
                ? "redirect:/home"
                : "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(){ return "login"; }
}
