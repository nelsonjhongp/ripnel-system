package com.ripnel.system.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        // Rutas públicas
        String uri = req.getRequestURI();
        if (uri.equals("/") || uri.equals("/login") || uri.startsWith("/css") || uri.startsWith("/img") || uri.startsWith("/js")) {
            return true;
        }
        // Sesión requerida
        Object user = req.getSession().getAttribute("USER");
        if (user == null) {
            res.sendRedirect("/login");
            return false;
        }
        return true;
    }
}