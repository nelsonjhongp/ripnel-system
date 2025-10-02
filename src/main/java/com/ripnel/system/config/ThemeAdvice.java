package com.ripnel.system.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ThemeAdvice {

    @ModelAttribute("theme")
    public String theme(HttpServletRequest req) {
        if (req.getCookies() == null) return "auto";
        for (Cookie c : req.getCookies()) {
            if ("theme".equals(c.getName())) {
                return c.getValue();
            }
        }
        return "auto";
    }
}
