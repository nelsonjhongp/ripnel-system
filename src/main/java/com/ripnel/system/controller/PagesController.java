package com.ripnel.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ripnel.system.model.*;
import com.ripnel.system.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PagesController {

    @Autowired private CategoryRepository categoryRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private InventoryMovementRepository movementRepo;
    @Autowired private ProductVariantRepository variantRepo;
    @Autowired private MaterialRepository materialRepo;
    @Autowired private PurchaseOrderRepository purchaseOrderRepo;
    @Autowired private SaleRepository saleRepo;

    // DTO para top productos
    public static class TopProductVM {
        private String label;
        private long qty;
        private double percent;

        public TopProductVM(String label, long qty, double percent) {
            this.label = label;
            this.qty = qty;
            this.percent = percent;
        }

        public String getLabel() { return label; }
        public long getQty() { return qty; }
        public double getPercent() { return percent; }
    }

    @GetMapping("/admin")
    public String admin(Model model, HttpSession s) throws Exception {

        User u = (User) s.getAttribute("USER");
        if (u == null) return "redirect:/login";

        LocalDateTime now = LocalDateTime.now();
        ObjectMapper mapper = new ObjectMapper();

        // ========================
        // KPIs
        // ========================
        long cats = categoryRepo.count();
        long prods = productRepo.count();
        Long stockTotal = variantRepo.sumGlobalStockQty();
        if (stockTotal == null) stockTotal = 0L;

        long mov7d = movementRepo.countSince(now.minusDays(7));

        BigDecimal ventas7d = saleRepo.sumTotalAmountSince(now.minusDays(7));
        if (ventas7d == null) ventas7d = BigDecimal.ZERO;

        long criticalCount = materialRepo.countCriticalMaterials();

        model.addAttribute("countCategories", cats);
        model.addAttribute("countProducts", prods);
        model.addAttribute("totalStockQty", stockTotal);
        model.addAttribute("countMovements7d", mov7d);
        model.addAttribute("totalSales7d", ventas7d);
        model.addAttribute("countCriticalMaterials", criticalCount);

        // ========================
        // Listas laterales
        // ========================
        model.addAttribute("recentMovements",
                movementRepo.findTop10ByOrderByCreatedAtDesc());

        model.addAttribute("criticalMaterials",
                materialRepo.findTop5CriticalMaterials());

        model.addAttribute("oldOpenOrders",
                purchaseOrderRepo.findTop5ByStatusOrderByOrderedAtAsc(
                        PurchaseOrderStatus.OPEN
                ));

        model.addAttribute("zeroStockVariants",
                variantRepo.findTop5ByStockQtyLessThanEqualOrderByStockQtyAsc(0L));

        // ========================
        // Ventas últimos 7 días
        // ========================
        List<Object[]> dailyRaw = saleRepo.sumDailySalesSince(now.minusDays(7));
        List<String> dailyLabels = new ArrayList<>();
        List<Double> dailyValues = new ArrayList<>();

        for (Object[] row : dailyRaw) {
            LocalDate d = ((java.sql.Date) row[0]).toLocalDate();
            BigDecimal amt = (BigDecimal) row[1];

            dailyLabels.add(d.toString());
            dailyValues.add(amt.doubleValue());
        }

        model.addAttribute("dailyLabels", mapper.writeValueAsString(dailyLabels));
        model.addAttribute("dailyValues", mapper.writeValueAsString(dailyValues));

        // ========================
        // Stock por categoría
        // ========================
        List<Object[]> catRaw = variantRepo.getStockByCategory();
        List<String> catLabels = new ArrayList<>();
        List<Long> catValues = new ArrayList<>();

        for (Object[] row : catRaw) {
            catLabels.add((String) row[0]);
            catValues.add(((Number) row[1]).longValue());
        }

        model.addAttribute("catLabels", mapper.writeValueAsString(catLabels));
        model.addAttribute("catValues", mapper.writeValueAsString(catValues));

        // ========================
        // Top productos 30 días
        // ========================
        List<Object[]> rawTop = saleRepo.findTopProductsByQtySince(now.minusDays(30));
        List<TopProductVM> topVM = new ArrayList<>();

        long totalQty = 0;
        for (Object[] r : rawTop) {
            Number n = (Number) r[1];
            totalQty += (n != null ? n.longValue() : 0L);
        }

        for (Object[] r : rawTop) {
            String label = (String) r[0];
            long qty = ((Number) r[1]).longValue();
            double p = (totalQty > 0 ? qty * 100.0 / totalQty : 0.0);
            topVM.add(new TopProductVM(label, qty, p));
        }

        // JSON para Chart.js
        List<String> topLabels = new ArrayList<>();
        List<Long> topQtys = new ArrayList<>();

        for (TopProductVM t : topVM) {
            topLabels.add(t.getLabel());
            topQtys.add(t.getQty());
        }

        model.addAttribute("topLabelsJson", mapper.writeValueAsString(topLabels));
        model.addAttribute("topQtysJson", mapper.writeValueAsString(topQtys));

        // Lista para progressbars
        model.addAttribute("topProducts30d", topVM);

        return "admin";
    }

    @GetMapping("/home")
    public String home(HttpSession session){
        if (session.getAttribute("USER") == null) {
            return "redirect:/login";
        }
        return "home";
    }
}
