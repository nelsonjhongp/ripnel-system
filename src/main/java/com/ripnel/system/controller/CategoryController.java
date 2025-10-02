package com.ripnel.system.controller;

import com.ripnel.system.model.Category;
import com.ripnel.system.repository.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller @RequestMapping("/admin/categories")
public class CategoryController {
    private final CategoryRepository repo;
    public CategoryController(CategoryRepository repo){ this.repo = repo; }

    @GetMapping public String list(Model model){
        model.addAttribute("items", repo.findAll());
        return "admin/categories-list";
    }
    @GetMapping("/new") public String form(Model model){
        model.addAttribute("category", new Category());
        return "admin/categories-form";
    }
    @PostMapping public String save(@ModelAttribute Category c){
        c.setActive(true);
        repo.save(c);
        return "redirect:/admin/categories";
    }
}
