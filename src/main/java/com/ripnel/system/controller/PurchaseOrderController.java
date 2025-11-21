package com.ripnel.system.controller;

import com.ripnel.system.dto.PurchaseOrderForm;
import com.ripnel.system.model.PurchaseOrder;
import com.ripnel.system.service.PurchaseOrderService;
import com.ripnel.system.repository.MaterialRepository;
import com.ripnel.system.repository.SupplierRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/compras/ordenes")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private SupplierRepository supplierRepo;

    @Autowired
    private MaterialRepository materialRepo;

    // LISTADO
    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", purchaseOrderService.findAll());
        return "compras/ordenes-list";
    }

    // FORM NUEVA OC
    @GetMapping("/nuevo")
    public String createForm(Model model) {
        PurchaseOrderForm form = new PurchaseOrderForm();
        form.addEmptyItems(5);

        model.addAttribute("form", form);
        model.addAttribute("suppliers", supplierRepo.findAll());
        model.addAttribute("materials", materialRepo.findAll());

        return "compras/ordenes-form";
    }

    // GUARDAR OC
    @PostMapping
    public String save(@ModelAttribute("form") @Valid PurchaseOrderForm form,
                       BindingResult br,
                       Model model) {

        if (br.hasErrors()) {
            model.addAttribute("suppliers", supplierRepo.findAll());
            model.addAttribute("materials", materialRepo.findAll());
            return "compras/ordenes-form";
        }

        PurchaseOrder saved = purchaseOrderService.createOrder(form);
        return "redirect:/compras/ordenes/" + saved.getId();
    }

    // DETALLE
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        PurchaseOrder order = purchaseOrderService.findById(id);
        model.addAttribute("order", order);
        return "compras/ordenes-detalle";
    }

    // MARCAR COMO RECIBIDA
    @PostMapping("/{id}/recibir")
    public String receive(@PathVariable Long id) {
        purchaseOrderService.receiveOrder(id);
        return "redirect:/compras/ordenes/" + id;
    }
}
