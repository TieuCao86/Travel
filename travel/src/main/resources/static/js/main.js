function enableDragScroll(container) {
  let isDragging = false;
  let startX = 0;
  let scrollLeft = 0;

  container.addEventListener("mousedown", (e) => {
    if (e.button !== 0) return;
    isDragging = true;
    container.classList.add("dragging");
    container.style.cursor = "grabbing";
    container.style.scrollBehavior = "auto";

    // Tạm thời bỏ pointer-events của tất cả con
    container.querySelectorAll("*").forEach(el => el.style.pointerEvents = "none");

    startX = e.pageX;
    scrollLeft = container.scrollLeft;
  });

  container.addEventListener("mousemove", (e) => {
    if (!isDragging) return;
    const move = e.pageX - startX;
    container.scrollLeft = scrollLeft - move;
  });

  const stopDrag = () => {
    if (!isDragging) return;
    isDragging = false;
    container.classList.remove("dragging");
    container.style.cursor = "grab";
    container.style.scrollBehavior = "smooth";

    // Bật lại pointer-events cho tất cả con
    container.querySelectorAll("*").forEach(el => el.style.pointerEvents = "auto");
  };

  container.addEventListener("mouseup", stopDrag);
  container.addEventListener("mouseleave", stopDrag);

  // Touch
  let touchStartX = 0;
  let touchScrollLeft = 0;

  container.addEventListener("touchstart", (e) => {
    touchStartX = e.touches[0].pageX;
    touchScrollLeft = container.scrollLeft;
    container.style.scrollBehavior = "auto";
  });

  container.addEventListener("touchmove", (e) => {
    const move = e.touches[0].pageX - touchStartX;
    container.scrollLeft = touchScrollLeft - move;
  });

  container.addEventListener("touchend", () => {
    container.style.scrollBehavior = "smooth";
  });
}

document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll(".scroll-drag-container").forEach(container => {
    enableDragScroll(container);
  });
});
