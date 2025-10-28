package com.ripnel.system.controller;

import com.ripnel.system.repository.CategoryRepository;
import com.ripnel.system.repository.ProductRepository;
import com.ripnel.system.repository.InventoryMovementRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class PagesController {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private InventoryMovementRepository movementRepo;

    @GetMapping("/admin")
    public String admin(Model model, HttpSession s){
        if (s.getAttribute("USER")==null) return "redirect:/login";

        long cats  = categoryRepo.count();
        long prods = productRepo.count();
        long mov7d = movementRepo.countSince(LocalDateTime.now().minusDays(7));

        model.addAttribute("countCategories", cats);
        model.addAttribute("countProducts", prods);
        model.addAttribute("countMovements7d", mov7d);

        model.addAttribute("recentMovements", movementRepo.findTop5ByOrderByCreatedAtDesc());

        return "admin";
    }

    @GetMapping("/home")
    public String home(HttpSession session){
        // puedes decidir aquí qué mostrar,
        // pero mínimo evita que entren si no hay sesión:
        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }
        return "home";
    }
}
