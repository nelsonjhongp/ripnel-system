package com.ripnel.system.controller;

import com.ripnel.system.model.Supplier;
import com.ripnel.system.repository.SupplierRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/compras/proveedores")
public class SupplierController {

    private final SupplierRepository supplierRepo;

    public SupplierController(SupplierRepository supplierRepo) {
        this.supplierRepo = supplierRepo;
    }

    // LISTA
    @GetMapping
    public String list(Model model, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        model.addAttribute("suppliers", supplierRepo.findAll());
        return "compras/proveedores-list";
    }

    // NUEVO
    @GetMapping("/nuevo")
    public String newForm(Model model, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        Supplier s = new Supplier();
        s.setActive(true);

        model.addAttribute("supplier", s);
        return "compras/proveedores-form";
    }

    // EDITAR
    @GetMapping("/{id}/editar")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        Supplier s = supplierRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));

        model.addAttribute("supplier", s);
        return "compras/proveedores-form";
    }

    // GUARDAR (nuevo o editar)
    @PostMapping("/guardar")
    public String save(@ModelAttribute("supplier") Supplier supplier,
                       HttpSession session) {

        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        supplierRepo.save(supplier);
        return "redirect:/compras/proveedores";
    }

    // ACTIVAR / DESACTIVAR
    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        Supplier s = supplierRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));

        s.setActive(!s.isActive());
        supplierRepo.save(s);

        return "redirect:/compras/proveedores";
    }
}
