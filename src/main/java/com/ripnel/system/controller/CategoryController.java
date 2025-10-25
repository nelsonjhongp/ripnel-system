package com.ripnel.system.controller;

import com.ripnel.system.model.Category;
import com.ripnel.system.repository.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categorias")
public class CategoryController {

    private final CategoryRepository repo;

    public CategoryController(CategoryRepository repo){
        this.repo = repo;
    }

    // LISTAR
    @GetMapping
    public String list(Model model){
        model.addAttribute("items", repo.findAll());
        return "admin/categorias-list";
    }

    // FORM NUEVA
    @GetMapping("/nuevo")
    public String formNew(Model model){
        model.addAttribute("cat", new Category());
        return "admin/categorias-form";
    }

    // GUARDAR NUEVA
    @PostMapping
    public String saveNew(@ModelAttribute("cat") Category c){
        if (c.getActive() == null) c.setActive(true);
        repo.save(c);
        return "redirect:/admin/categorias";
    }

    // FORM EDITAR
    @GetMapping("/{id}/editar")
    public String formEdit(@PathVariable Long id, Model model){
        Category c = repo.findById(id).orElseThrow();
        model.addAttribute("cat", c);
        return "admin/categorias-form";
    }

    // GUARDAR CAMBIOS
    @PostMapping("/{id}")
    public String saveEdit(@PathVariable Long id, @ModelAttribute("cat") Category c){
        c.setId(id);
        if (c.getActive() == null) c.setActive(true);
        repo.save(c);
        return "redirect:/admin/categorias";
    }

    // ELIMINAR
    @PostMapping("/{id}/eliminar")
    public String delete(@PathVariable Long id){
        repo.deleteById(id);
        return "redirect:/admin/categorias";
    }
}
