package com.ripnel.system.controller;

import com.ripnel.system.model.Location;
import com.ripnel.system.model.ProductVariant;
import com.ripnel.system.service.InventoryService;
import com.ripnel.system.repository.LocationRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/almacen/stock")
public class StockController {

    private final InventoryService inventoryService;
    private final LocationRepository locationRepository;

    public StockController(InventoryService inventoryService,
                           LocationRepository locationRepository) {
        this.inventoryService = inventoryService;
        this.locationRepository = locationRepository;
    }

    @GetMapping
    public String viewStock(@RequestParam(name = "locationId", required = false) Long locationId,
                            Model model,
                            HttpSession session) {

        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }

        // Todas las ubicaciones para el combo
        List<Location> locations = locationRepository.findAll();
        model.addAttribute("locations", locations);

        // Elegir ubicación por defecto (la primera) si no mandan ninguna
        Location selectedLocation = null;
        if (locationId != null) {
            selectedLocation = locationRepository.findById(locationId).orElse(null);
        } else if (!locations.isEmpty()) {
            selectedLocation = locations.get(0);
            locationId = selectedLocation.getId();
        }

        model.addAttribute("selectedLocationId", locationId);
        model.addAttribute("selectedLocation", selectedLocation);

        // Si no hay ubicación seleccionada aún, mostrar tabla vacía
        List<StockRow> rows = new ArrayList<>();

        if (selectedLocation != null) {
            Map<ProductVariant, Integer> stockMap =
                    inventoryService.getStockForLocation(selectedLocation);

            for (Map.Entry<ProductVariant, Integer> e : stockMap.entrySet()) {
                ProductVariant v = e.getKey();
                Integer qty = e.getValue();

                StockRow row = new StockRow();
                row.setSku(v.getSku());
                row.setProductName(v.getProduct().getName());
                row.setColorName(v.getColor().getName());
                row.setSizeCode(v.getSize().getCode());
                row.setQuantity(qty != null ? qty : 0);

                rows.add(row);
            }
        }

        model.addAttribute("rows", rows);

        return "almacen/stock-list";
    }

    // DTO para la tabla
    public static class StockRow {
        private String sku;
        private String productName;
        private String colorName;
        private String sizeCode;
        private Integer quantity;

        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public String getColorName() { return colorName; }
        public void setColorName(String colorName) { this.colorName = colorName; }

        public String getSizeCode() { return sizeCode; }
        public void setSizeCode(String sizeCode) { this.sizeCode = sizeCode; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}
