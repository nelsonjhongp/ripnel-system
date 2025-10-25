package com.ripnel.system.controller;

import com.ripnel.system.model.InventoryMovement;
import com.ripnel.system.model.MovementType;
import com.ripnel.system.repository.InventoryMovementRepository;
import com.ripnel.system.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/almacen/movimientos")
public class InventoryController {
    /*

    private final InventoryMovementRepository movementRepo;
    private final ProductRepository productRepo;

    public InventoryController(InventoryMovementRepository movementRepo,
                               ProductRepository productRepo) {
        this.movementRepo = movementRepo;
        this.productRepo = productRepo;
    }

    // LISTA + FORM EN LA MISMA VISTA
    @GetMapping
    public String view(Model model) {
        model.addAttribute("movList", movementRepo.findTop20ByOrderByCreatedAtDesc());
        model.addAttribute("prodList", productRepo.findAll()); // para el combo
        model.addAttribute("newMov", new InventoryMovement());
        model.addAttribute("types", MovementType.values());    // IN / OUT
        return "almacen/movimientos-list";
    }

    // REGISTRAR MOVIMIENTO
    @PostMapping
    public String create(@ModelAttribute("newMov") InventoryMovement mov) {
        mov.setCreatedAt(java.time.LocalDateTime.now());
        movementRepo.save(mov);
        return "redirect:/almacen/movimientos";
    }

     */
}
