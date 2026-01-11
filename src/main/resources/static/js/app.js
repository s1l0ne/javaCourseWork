(function(){
  const key = "nl2sql_ui_theme";
  const btn = document.getElementById("themeToggle");
  const hl  = document.getElementById("hlTheme");

  const setHl = (mode) => {
    if (!hl) return;
    hl.href = (mode === "light")
      ? "https://cdn.jsdelivr.net/npm/highlight.js@11.9.0/styles/github.min.css"
      : "https://cdn.jsdelivr.net/npm/highlight.js@11.9.0/styles/github-dark.min.css";
  };

  const apply = (mode) => {
    document.body.classList.toggle("theme-light", mode === "light");
    document.body.classList.toggle("theme-dark", mode !== "light");
    if (btn) btn.textContent = (mode === "light") ? "Тёмная тема" : "Светлая тема";
    setHl(mode);
  };

  let mode = localStorage.getItem(key) || "dark";
  apply(mode);

  if (btn){
    btn.addEventListener("click", () => {
      mode = (mode === "light") ? "dark" : "light";
      localStorage.setItem(key, mode);
      apply(mode);
      if (window.hljs) hljs.highlightAll();
    });
  }

  document.addEventListener("click", async (e) => {
    const t = e.target;
    const b = t && t.closest ? t.closest("[data-copy]") : null;
    if (!b) return;
    const sel = b.getAttribute("data-copy");
    const el = sel ? document.querySelector(sel) : null;
    if (!el) return;
    const text = el.innerText || el.textContent || "";
    try{
      await navigator.clipboard.writeText(text);
      const old = b.textContent;
      b.textContent = "Скопировано";
      setTimeout(()=> b.textContent = old, 900);
    }catch(_){}
  });
})();
