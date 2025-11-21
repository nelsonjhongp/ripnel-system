package com.ripnel.system.controller;

import com.ripnel.system.model.*;
import com.ripnel.system.repository.LocationRepository;
import com.ripnel.system.repository.ProductVariantRepository;
import com.ripnel.system.repository.SaleRepository;
import com.ripnel.system.service.InventoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ventas")
public class SalesController {

    private final SaleRepository saleRepo;
    private final ProductVariantRepository variantRepo;
    private final LocationRepository locationRepo;
    private final InventoryService inventoryService;

    public SalesController(SaleRepository saleRepo,
                           ProductVariantRepository variantRepo,
                           LocationRepository locationRepo,
                           InventoryService inventoryService) {
        this.saleRepo = saleRepo;
        this.variantRepo = variantRepo;
        this.locationRepo = locationRepo;
        this.inventoryService = inventoryService;
    }

    // --- raíz /ventas → redirige al form ---
    @GetMapping
    public String root() {
        return "redirect:/ventas/form";
    }

    // --- formulario de nueva venta ---
    @GetMapping("/form")
    public String form(Model model, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        model.addAttribute("variants", variantRepo.findAll());
        model.addAttribute("locations", locationRepo.findAll());
        model.addAttribute("form", new SaleForm());

        // si quieres mostrar últimas N en el formulario:
        List<Sale> recent = saleRepo.findTop5ByOrderByDateCreatedDesc();
        model.addAttribute("recentSales", recent);

        return "ventas/ventas-form";
    }

    // --- registrar venta ---
    @PostMapping
    @Transactional
    public String registerSale(@ModelAttribute("form") SaleForm form,
                               HttpSession session) {

        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        if (form.getItems() == null || form.getItems().isEmpty()) {
            return "redirect:/ventas/form";
        }

        Location location = locationRepo.findById(form.getLocationId())
                .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada"));

        Sale sale = new Sale();
        sale.setDateCreated(LocalDateTime.now());
        sale.setLocation(location);
        sale.setNote(form.getNote());

        BigDecimal total = BigDecimal.ZERO;

        for (SaleItemForm itemForm : form.getItems()) {
            if (itemForm == null) continue;
            if (itemForm.getVariantId() == null) continue;
            if (itemForm.getQuantity() == null || itemForm.getQuantity() <= 0) continue;
            if (itemForm.getPrice() == null) continue;

            ProductVariant variant = variantRepo.findById(itemForm.getVariantId())
                    .orElseThrow(() -> new IllegalArgumentException("Variante no encontrada"));

            BigDecimal subtotal = itemForm.getPrice()
                    .multiply(BigDecimal.valueOf(itemForm.getQuantity()));

            SaleItem si = new SaleItem();
            si.setSale(sale);
            si.setVariant(variant);
            si.setQuantity(itemForm.getQuantity());
            si.setPriceUnit(itemForm.getPrice());
            si.setSubtotal(subtotal);

            sale.getItems().add(si);
            total = total.add(subtotal);

            // movimiento de inventario
            InventoryMovement mov = new InventoryMovement();
            mov.setMovementType(MovementType.OUT);
            mov.setVariant(variant);
            mov.setLocation(location);
            mov.setQuantity(itemForm.getQuantity());
            mov.setCreatedAt(LocalDateTime.now());

            String note = (form.getNote() != null && !form.getNote().isBlank())
                    ? "VENTA: " + form.getNote()
                    : "VENTA";
            mov.setNote(note);

            inventoryService.registerMovement(mov);
        }

        if (sale.getItems().isEmpty()) {
            return "redirect:/ventas/form";
        }

        sale.setTotalAmount(total);
        saleRepo.save(sale);

        return "redirect:/ventas/lista";
    }

    // --- listado con filtros ---
    @GetMapping("/lista")
    public String list(
            @RequestParam(value = "locationId", required = false) Long locationId,
            @RequestParam(value = "desde", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(value = "hasta", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            Model model,
            HttpSession session
    ) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        List<Sale> all = saleRepo.findAllByOrderByDateCreatedDesc();

        // filtrito en memoria
        List<Sale> filtered = all.stream()
                .filter(s -> {
                    if (locationId != null && s.getLocation() != null) {
                        if (!s.getLocation().getId().equals(locationId)) {
                            return false;
                        }
                    }
                    if (desde != null) {
                        LocalDate d = s.getDateCreated().toLocalDate();
                        if (d.isBefore(desde)) return false;
                    }
                    if (hasta != null) {
                        LocalDate d = s.getDateCreated().toLocalDate();
                        if (d.isAfter(hasta)) return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        model.addAttribute("locations", locationRepo.findAll());
        model.addAttribute("sales", filtered);
        model.addAttribute("locationId", locationId);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);

        return "ventas/ventas-list";
    }

    // --- detalle de una venta ---
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        Sale sale = saleRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));

        model.addAttribute("sale", sale);
        return "ventas/ventas-detalle";
    }

    // DTOs internos
    public static class SaleForm {
        private Long locationId;
        private String note;
        private List<SaleItemForm> items = new ArrayList<>();

        public Long getLocationId() { return locationId; }
        public void setLocationId(Long locationId) { this.locationId = locationId; }

        public String getNote() { return note; }
        public void setNote(String note) { this.note = note; }

        public List<SaleItemForm> getItems() { return items; }
        public void setItems(List<SaleItemForm> items) { this.items = items; }
    }

    public static class SaleItemForm {
        private Long variantId;
        private Integer quantity;
        private BigDecimal price;

        public Long getVariantId() { return variantId; }
        public void setVariantId(Long variantId) { this.variantId = variantId; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
    }
}
