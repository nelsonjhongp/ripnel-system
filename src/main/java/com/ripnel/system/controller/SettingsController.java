package com.ripnel.system.controller;// imports necesarios
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SettingsController {

    @GetMapping("/settings")
    public String settings(
            @CookieValue(name="theme", required=false, defaultValue="auto") String theme,
            Model model) {
        model.addAttribute("theme", theme); // "light", "dark" o "auto"
        return "settings";
    }

    @PostMapping("/settings")
    public String saveSettings(@RequestParam("theme") String theme,
                               HttpServletResponse response) {
        // validamos valor
        if (!theme.equals("light") && !theme.equals("dark") && !theme.equals("auto")) {
            theme = "auto";
        }
        Cookie c = new Cookie("theme", theme);
        c.setPath("/");
        c.setMaxAge(60*60*24*365); // 1 año
        c.setHttpOnly(false);      // lo puede leer JS si lo necesitas
        response.addCookie(c);
        return "redirect:/settings?ok=1";
    }
}
