package com.ripnel.system.controller;

import com.ripnel.system.model.InventoryMovement;
import com.ripnel.system.model.MovementType;
import com.ripnel.system.repository.InventoryMovementRepository;
import com.ripnel.system.repository.LocationRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/almacen/movimientos")
public class InventoryMovementController {

    private final InventoryMovementRepository movRepo;
    private final LocationRepository locationRepo;

    public InventoryMovementController(
            InventoryMovementRepository movRepo,
            LocationRepository locationRepo
    ) {
        this.movRepo = movRepo;
        this.locationRepo = locationRepo;
    }

    // LISTA + FORM
    @GetMapping
    public String list(Model model, HttpSession session) {

        // seguridad básica
        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        // movimientos recientes
        model.addAttribute("movements",
                movRepo.findTop20ByOrderByCreatedAtDesc());

        // ubicaciones para el combo del formulario
        model.addAttribute("locations",
                locationRepo.findAll());

        // objeto vacío para el formulario
        model.addAttribute("form", new MovementForm());

        // para el <select> de tipo
        model.addAttribute("types", MovementType.values());

        return "almacen/movimientos-list"; // vista thymeleaf
    }

    // REGISTRAR NUEVO MOVIMIENTO
    @PostMapping
    public String create(@ModelAttribute("form") MovementForm form,
                         HttpSession session) {

        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        InventoryMovement m = new InventoryMovement();
        m.setCreatedAt(LocalDateTime.now());
        m.setMovementType(form.getMovementType()); // IN / OUT / TRANSFER
        m.setSku(form.getSku());
        m.setQuantity(form.getQuantity());
        m.setNote(form.getNote());
        m.setLocation(
                locationRepo.findById(form.getLocationId()).orElse(null)
        );

        movRepo.save(m);

        // TODO en otra iteración: actualizar stock_levels_sku automáticamente

        return "redirect:/almacen/movimientos";
    }

    // DTO interno simple para el formulario
    public static class MovementForm {
        private MovementType movementType;
        private String sku;
        private Integer quantity;
        private Long locationId;
        private String note;

        public MovementType getMovementType() { return movementType; }
        public void setMovementType(MovementType movementType) { this.movementType = movementType; }

        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public Long getLocationId() { return locationId; }
        public void setLocationId(Long locationId) { this.locationId = locationId; }

        public String getNote() { return note; }
        public void setNote(String note) { this.note = note; }
    }
}
