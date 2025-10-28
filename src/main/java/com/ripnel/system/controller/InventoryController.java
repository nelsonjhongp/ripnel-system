package com.ripnel.system.controller;

import com.ripnel.system.model.InventoryMovement;
import com.ripnel.system.model.Location;
import com.ripnel.system.model.MovementType;
import com.ripnel.system.model.ProductVariant;
import com.ripnel.system.repository.InventoryMovementRepository;
import com.ripnel.system.repository.LocationRepository;
import com.ripnel.system.repository.ProductVariantRepository;
import com.ripnel.system.service.InventoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/almacen/movimientos")
public class InventoryController {

    private final InventoryMovementRepository movementRepo;
    private final ProductVariantRepository variantRepo;
    private final LocationRepository locationRepo;
    private final InventoryService inventoryService;

    public InventoryController(
            InventoryMovementRepository movementRepo,
            ProductVariantRepository variantRepo,
            LocationRepository locationRepo,
            InventoryService inventoryService
    ) {
        this.movementRepo = movementRepo;
        this.variantRepo = variantRepo;
        this.locationRepo = locationRepo;
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public String view(Model model, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        model.addAttribute("movements", movementRepo.findTop20ByOrderByCreatedAtDesc());
        model.addAttribute("variants", variantRepo.findAll());
        model.addAttribute("locations", locationRepo.findAll());

        model.addAttribute("form", new MovementForm());

        return "almacen/movimientos-list";
    }

    @PostMapping
    public String create(@ModelAttribute("form") MovementForm form,
                         HttpSession session) {

        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        // Construimos el movimiento desde el form
        InventoryMovement mov = new InventoryMovement();
        mov.setMovementType(form.getMovementType());
        mov.setQuantity(form.getQuantity());
        mov.setNote(form.getNote());
        mov.setCreatedAt(LocalDateTime.now());

        // variant
        ProductVariant var = variantRepo.findById(form.getVariantId()).orElseThrow();
        mov.setVariant(var);

        // location
        Location loc = locationRepo.findById(form.getLocationId()).orElse(null);
        mov.setLocation(loc);

        // Guardar y actualizar stock
        inventoryService.registerMovement(mov);

        return "redirect:/almacen/movimientos";
    }

    // DTO interno para el form
    public static class MovementForm {
        private MovementType movementType;
        private Long variantId;
        private Integer quantity;
        private Long locationId;
        private String note;

        public MovementType getMovementType() { return movementType; }
        public void setMovementType(MovementType movementType) { this.movementType = movementType; }

        public Long getVariantId() { return variantId; }
        public void setVariantId(Long variantId) { this.variantId = variantId; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public Long getLocationId() { return locationId; }
        public void setLocationId(Long locationId) { this.locationId = locationId; }

        public String getNote() { return note; }
        public void setNote(String note) { this.note = note; }
    }
}
