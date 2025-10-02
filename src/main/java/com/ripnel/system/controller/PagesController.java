package com.ripnel.system.controller;// controller/PagesController.java (añade deps y action /admin)
import com.ripnel.system.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class PagesController {

    @Autowired private CategoryRepository categoryRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private InventoryMovementRepository movementRepo;

    @GetMapping("/admin")
    public String admin(Model model, HttpSession s){
        if (s.getAttribute("USER")==null) return "redirect:/login"; // protección simple
        long cats  = categoryRepo.count();
        long prods = productRepo.count();
        long mov7d = movementRepo.countSince(java.time.LocalDateTime.now().minusDays(7));
        model.addAttribute("countCategories", cats);
        model.addAttribute("countProducts", prods);
        model.addAttribute("countMovements7d", mov7d);
        model.addAttribute("recentMovements", movementRepo.findTop5ByOrderByCreatedAtDesc());
        return "admin";
    }

    @GetMapping("/home")
    public String home(){ return "home"; }
}