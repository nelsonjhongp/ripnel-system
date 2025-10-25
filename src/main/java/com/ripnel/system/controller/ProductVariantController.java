package com.ripnel.system.controller;

import com.ripnel.system.model.*;
import com.ripnel.system.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/productos/{productId}/variantes")
public class ProductVariantController {

    private final ProductRepository productRepo;
    private final ProductVariantRepository variantRepo;
    private final ColorRepository colorRepo;
    private final SizeRepository sizeRepo;

    public ProductVariantController(
            ProductRepository productRepo,
            ProductVariantRepository variantRepo,
            ColorRepository colorRepo,
            SizeRepository sizeRepo
    ) {
        this.productRepo = productRepo;
        this.variantRepo = variantRepo;
        this.colorRepo = colorRepo;
        this.sizeRepo  = sizeRepo;
    }

    // LISTAR VARIANTES DE UN PRODUCTO
    @GetMapping
    public String list(@PathVariable Long productId,
                       HttpSession session,
                       Model model) {

        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        Optional<Product> prodOpt = productRepo.findById(productId);
        if (prodOpt.isEmpty()) {
            return "redirect:/admin/productos";
        }

        Product product = prodOpt.get();

        model.addAttribute("product", product);
        model.addAttribute("variants",
                variantRepo.findByProductAndActiveTrueOrderByColor_NameAscSize_CodeAsc(product));

        // para el formulario de creación
        model.addAttribute("variantForm", new ProductVariant());
        model.addAttribute("allColors", colorRepo.findAll());
        model.addAttribute("allSizes",  sizeRepo.findAll());

        return "admin/variantes-list";
    }

    // CREAR NUEVA VARIANTE
    @PostMapping
    public String create(@PathVariable Long productId,
                         @RequestParam Long colorId,
                         @RequestParam Long sizeId,
                         @RequestParam String sku,
                         @RequestParam(required=false) String price,
                         HttpSession session) {

        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        Product product = productRepo.findById(productId).orElse(null);
        if (product == null) {
            return "redirect:/admin/productos";
        }

        Color color = colorRepo.findById(colorId).orElse(null);
        Size size   = sizeRepo.findById(sizeId).orElse(null);
        if (color == null || size == null) {
            return "redirect:/admin/productos/" + productId + "/variantes";
        }

        ProductVariant pv = new ProductVariant();
        pv.setProduct(product);
        pv.setColor(color);
        pv.setSize(size);
        pv.setSku(sku);
        pv.setActive(true);

        if (price != null && !price.isBlank()) {
            pv.setPrice(new java.math.BigDecimal(price));
        }

        variantRepo.save(pv);

        return "redirect:/admin/productos/" + productId + "/variantes";
    }

    // DESACTIVAR VARIANTE
    @PostMapping("/{variantId}/desactivar")
    public String deactivate(@PathVariable Long productId,
                             @PathVariable Long variantId,
                             HttpSession session) {

        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        variantRepo.findById(variantId).ifPresent(v -> {
            v.setActive(false);
            variantRepo.save(v);
        });

        return "redirect:/admin/productos/" + productId + "/variantes";
    }
}
