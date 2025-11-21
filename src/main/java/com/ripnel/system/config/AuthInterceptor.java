package com.ripnel.system.config;

import com.ripnel.system.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String uri = request.getRequestURI();

        // 🔥 CORRECCIÓN: la sesión guarda "USER", no "user"
        User user = (User) request.getSession().getAttribute("USER");

        // ➤ RUTAS PUBLICAS
        if (uri.startsWith("/login") ||
                uri.startsWith("/auth") ||
                uri.startsWith("/css") ||
                uri.startsWith("/js") ||
                uri.startsWith("/img")) {
            return true;
        }

        // ➤ SIN SESIÓN → login
        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        // ➤ ADMIN TIENE TODO
        if (user.hasRole("ADMIN")) {
            return true;
        }

        // ➤ REGLAS POR ROL
        if (uri.startsWith("/compras") &&
                !user.hasRole("COMPRAS")) {
            response.sendRedirect("/unauthorized");
            return false;
        }

        if (uri.startsWith("/almacen") &&
                !user.hasRole("ALMACEN")) {
            response.sendRedirect("/unauthorized");
            return false;
        }

        if (uri.startsWith("/produccion") &&
                !user.hasRole("PRODUCCION")) {
            response.sendRedirect("/unauthorized");
            return false;
        }

        if (uri.startsWith("/ventas") &&
                !(user.hasRole("VENTAS") || user.hasRole("VENDEDOR"))) {
            response.sendRedirect("/unauthorized");
            return false;
        }

        return true;
    }
}
