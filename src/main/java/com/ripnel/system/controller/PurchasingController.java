package com.ripnel.system.controller;

import com.ripnel.system.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PurchasingController {

    private final ProductRepository productRepo;

    public PurchasingController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping("/compras/reabastecer")
    public String viewLowStock(Model model, HttpSession session) {

        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        int MIN_STOCK = 5; // umbral configurable
        model.addAttribute("minStock", MIN_STOCK);
        model.addAttribute("lowStockList", productRepo.findLowStock(MIN_STOCK));

        return "compras/reabastecer-list"; // nueva vista
    }
}
