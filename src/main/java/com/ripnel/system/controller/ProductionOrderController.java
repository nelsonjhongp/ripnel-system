package com.ripnel.system.controller;

import com.ripnel.system.model.Location;
import com.ripnel.system.model.ProductionOrder;
import com.ripnel.system.repository.LocationRepository;
import com.ripnel.system.repository.ProductionOrderRepository;
import com.ripnel.system.service.ProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/produccion/ordenes")
public class ProductionOrderController {

    @Autowired
    private ProductionOrderRepository orderRepo;

    @Autowired
    private ProductionService productionService;

    @Autowired
    private LocationRepository locationRepo; // si ya lo tienes, si no te lo paso

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderRepo.findAll());
        return "produccion/ordenes-list";
    }

    @GetMapping("/nueva")
    public String createForm(Model model) {
        ProductionOrder order = new ProductionOrder();
        List<Location> talleres = locationRepo.findAll(); // puedes filtrar luego por tipo

        model.addAttribute("order", order);
        model.addAttribute("talleres", talleres);
        return "produccion/ordenes-form";
    }

    @PostMapping("/guardar")
    public String save(@ModelAttribute("order") ProductionOrder order) {
        productionService.createOrder(order);
        return "redirect:/produccion/ordenes";
    }

    @GetMapping("/{id}/iniciar")
    public String start(@PathVariable Long id) {
        productionService.startProduction(id);
        return "redirect:/produccion/ordenes";
    }

    @GetMapping("/{id}/consumir")
    public String consume(@PathVariable Long id) {
        productionService.consumeMaterials(id);
        return "redirect:/produccion/ordenes";
    }

    @GetMapping("/{id}/finalizar")
    public String finish(@PathVariable Long id) {
        productionService.finishProduction(id);
        return "redirect:/produccion/ordenes";
    }
}
