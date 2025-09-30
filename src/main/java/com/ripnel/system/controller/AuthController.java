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

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("error", null);
        return "login"; // templates/login.html
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          Model model,
                          HttpSession session) {
        return authService.login(email, password)
                .map(u -> {
                    session.setAttribute("user", u);
                    if (u.hasRole("ADMIN"))       return "redirect:/admin";
                    if (u.hasRole("ALMACEN"))     return "redirect:/almacen";
                    if (u.hasRole("COMPRAS"))     return "redirect:/compras";
                    if (u.hasRole("PRODUCCION"))  return "redirect:/produccion";
                    if (u.hasRole("VENDEDOR"))    return "redirect:/ventas";
                    return "redirect:/home";
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
    public String root(){ return "redirect:/login"; }
}
