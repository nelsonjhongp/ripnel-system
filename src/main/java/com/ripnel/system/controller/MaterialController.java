package com.ripnel.system.controller;

import com.ripnel.system.model.Material;
import com.ripnel.system.model.Supplier;
import com.ripnel.system.repository.MaterialRepository;
import com.ripnel.system.repository.SupplierRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/compras/materiales")
public class MaterialController {

    private final MaterialRepository materialRepo;
    private final SupplierRepository supplierRepo;

    public MaterialController(MaterialRepository materialRepo,
                              SupplierRepository supplierRepo) {
        this.materialRepo = materialRepo;
        this.supplierRepo = supplierRepo;
    }

    // LISTA
    @GetMapping
    public String list(Model model, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        model.addAttribute("materials", materialRepo.findAll());
        return "compras/materiales-list";
    }

    // NUEVO
    @GetMapping("/nuevo")
    public String newForm(Model model, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        MaterialForm form = new MaterialForm();
        form.setActive(true);

        model.addAttribute("form", form);
        model.addAttribute("suppliers", supplierRepo.findAll());

        return "compras/materiales-form";
    }

    // EDITAR
    @GetMapping("/{id}/editar")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        Material material = materialRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Material no encontrado"));

        MaterialForm form = new MaterialForm();
        form.setId(material.getId());
        form.setCode(material.getCode());
        form.setName(material.getName());
        form.setUnit(material.getUnit());
        form.setMinStock(material.getMinStock());
        form.setStockQty(material.getStockQty());
        form.setCost(material.getCost());
        form.setActive(material.isActive());
        form.setSupplierId(material.getSupplier() != null ? material.getSupplier().getId() : null);

        model.addAttribute("form", form);
        model.addAttribute("suppliers", supplierRepo.findAll());

        return "compras/materiales-form";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String save(@ModelAttribute("form") MaterialForm form,
                       HttpSession session) {

        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        Material material = (form.getId() != null)
                ? materialRepo.findById(form.getId())
                .orElseThrow(() -> new IllegalArgumentException("Material no encontrado"))
                : new Material();

        material.setCode(form.getCode());
        material.setName(form.getName());
        material.setUnit(form.getUnit());
        material.setMinStock(form.getMinStock());
        material.setStockQty(form.getStockQty());
        material.setCost(form.getCost());
        material.setActive(form.isActive());

        if (form.getSupplierId() != null) {
            Supplier supplier = supplierRepo.findById(form.getSupplierId())
                    .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));
            material.setSupplier(supplier);
        } else {
            material.setSupplier(null);
        }

        materialRepo.save(material);

        return "redirect:/compras/materiales";
    }

    // ACTIVAR / DESACTIVAR
    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        Material material = materialRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Material no encontrado"));

        material.setActive(!material.isActive());
        materialRepo.save(material);

        return "redirect:/compras/materiales";
    }

    // DTO interno
    public static class MaterialForm {
        private Long id;
        private String code;
        private String name;
        private String unit;
        private Double minStock;
        private Double stockQty;
        private Double cost;
        private Long supplierId;
        private boolean active;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }

        public Double getMinStock() { return minStock; }
        public void setMinStock(Double minStock) { this.minStock = minStock; }

        public Double getStockQty() { return stockQty; }
        public void setStockQty(Double stockQty) { this.stockQty = stockQty; }

        public Double getCost() { return cost; }
        public void setCost(Double cost) { this.cost = cost; }

        public Long getSupplierId() { return supplierId; }
        public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }
}
