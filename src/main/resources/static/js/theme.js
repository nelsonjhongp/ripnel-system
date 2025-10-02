// /static/js/theme.js
(() => {
  const root = document.body;
  const KEY = "ripnel-theme";

  // Set inicial desde localStorage (default: light)
  const saved = localStorage.getItem(KEY);
  if (saved) root.setAttribute("data-bs-theme", saved);
  else root.setAttribute("data-bs-theme", "light");

  function render() {
    const cur = root.getAttribute("data-bs-theme") || "light";
    const icon = document.querySelector("#themeIcon");
    const text = document.querySelector("#themeText");
    if (icon && text) {
      if (cur === "dark") { icon.textContent = "🌙"; text.textContent = "Oscuro"; }
      else { icon.textContent = "☀️"; text.textContent = "Claro"; }
    }
  }

  document.addEventListener("click", (e) => {
    const btn = e.target.closest("#themeToggle");
    if (!btn) return;
    const cur = root.getAttribute("data-bs-theme") || "light";
    const next = cur === "dark" ? "light" : "dark";
    root.setAttribute("data-bs-theme", next);
    localStorage.setItem(KEY, next);
    render();
  });

  document.addEventListener("DOMContentLoaded", render);
})();
