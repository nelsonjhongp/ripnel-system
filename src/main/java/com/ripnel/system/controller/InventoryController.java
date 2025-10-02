package com.ripnel.system.controller;

import com.ripnel.system.model.MovementType;
import com.ripnel.system.repository.ProductRepository;
import com.ripnel.system.service.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller @RequestMapping("/almacen/movimientos")
public class InventoryController {
    private final InventoryService service;
    private final ProductRepository productRepo;
    public InventoryController(InventoryService s, ProductRepository p){ this.service=s; this.productRepo=p; }

    @GetMapping public String form(Model model){
        model.addAttribute("products", productRepo.findAll());
        model.addAttribute("types", MovementType.values());
        return "almacen/movements";
    }
    @PostMapping public String register(@RequestParam Long productId,
                                        @RequestParam MovementType type,
                                        @RequestParam int quantity,
                                        @RequestParam(required=false) String note){
        service.registerMovement(productId, type, quantity, note);
        return "redirect:/almacen/movimientos";
    }
}