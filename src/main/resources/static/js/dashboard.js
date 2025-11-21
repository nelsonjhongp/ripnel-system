document.addEventListener("DOMContentLoaded", () => {

    // Ventas últimos 7 días
    const labels7d = JSON.parse(document.getElementById("dailyLabelsJson").textContent || "[]");
    const values7d = JSON.parse(document.getElementById("dailyValuesJson").textContent || "[]");

    if (labels7d.length > 0) {
        new Chart(document.getElementById("chartSales7d"), {
            type: "line",
            data: {
                labels: labels7d,
                datasets: [{
                    label: "Ventas (S/)",
                    data: values7d,
                    borderWidth: 2,
                    tension: 0.3
                }]
            }
        });
    }

    // Stock x categoría
    const catLabels = JSON.parse(document.getElementById("catLabelsJson").textContent || "[]");
    const catValues = JSON.parse(document.getElementById("catValuesJson").textContent || "[]");

    new Chart(document.getElementById("chartStockCategory"), {
        type: "bar",
        data: {
            labels: catLabels,
            datasets: [{
                label: "Unidades",
                data: catValues,
                borderWidth: 1
            }]
        }
    });

    // Top productos
    const topLabels = JSON.parse(document.getElementById("topLabelsJson").textContent || "[]");
    const topQtys = JSON.parse(document.getElementById("topQtysJson").textContent || "[]");

    if (topLabels.length > 0) {
        new Chart(document.getElementById("chartTopProducts"), {
            type: "bar",
            data: {
                labels: topLabels,
                datasets: [{
                    label: "Unidades vendidas",
                    data: topQtys
                }]
            },
            options: { indexAxis: "y" }
        });
    }
});
