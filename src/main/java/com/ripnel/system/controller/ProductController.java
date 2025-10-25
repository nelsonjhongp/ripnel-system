package com.ripnel.system.controller;

import com.ripnel.system.model.Product;
import com.ripnel.system.repository.ProductRepository;
import com.ripnel.system.repository.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/productos")
public class ProductController {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public ProductController(ProductRepository productRepo,
                             CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    // LISTAR
    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", productRepo.findAll());
        return "admin/productos-list";
    }

    // FORM NUEVO
    @GetMapping("/nuevo")
    public String formNew(Model model) {
        model.addAttribute("prod", new Product());
        model.addAttribute("cats", categoryRepo.findAll());
        return "admin/productos-form";
    }

    // GUARDAR NUEVO
    @PostMapping
    public String saveNew(@ModelAttribute("prod") Product p) {
        // si no marcaron el checkbox active, viene null -> default true
        if (p.getActive() == null) p.setActive(true);
        if (p.getStockQty() == null) p.setStockQty(0);
        productRepo.save(p);
        return "redirect:/admin/productos";
    }

    // FORM EDITAR
    @GetMapping("/{id}/editar")
    public String formEdit(@PathVariable Long id, Model model) {
        Product p = productRepo.findById(id).orElseThrow();
        model.addAttribute("prod", p);
        model.addAttribute("cats", categoryRepo.findAll());
        return "admin/productos-form";
    }

    // GUARDAR EDICIÓN
    @PostMapping("/{id}")
    public String saveEdit(@PathVariable Long id, @ModelAttribute("prod") Product p) {
        p.setId(id);
        if (p.getActive() == null) p.setActive(true);
        if (p.getStockQty() == null) p.setStockQty(0);
        productRepo.save(p);
        return "redirect:/admin/productos";
    }

    // ELIMINAR
    @PostMapping("/{id}/eliminar")
    public String delete(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/admin/productos";
    }
}