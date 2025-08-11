function enableDragScroll(container) {
  let isDragging = false;
  let startX = 0;
  let scrollLeft = 0;

  // Chuột
  container.addEventListener("mousedown", (e) => {
    if (e.button !== 0) return; // chỉ nút trái
    isDragging = true;
    container.classList.add("dragging");
    container.style.cursor = "grabbing";
    container.style.scrollSnapType = "none"; // tạm tắt snap
    container.style.scrollBehavior = "auto"; // tắt smooth khi kéo
    startX = e.pageX;
    scrollLeft = container.scrollLeft;
  });

  container.addEventListener("mousemove", (e) => {
    if (!isDragging) return;
    const x = e.pageX;
    const move = x - startX;
    container.scrollLeft = scrollLeft - move;
  });

  const stopDrag = () => {
    if (!isDragging) return;
    isDragging = false;
    container.classList.remove("dragging");
    container.style.cursor = "grab";
    container.style.scrollSnapType = "x mandatory";
    container.style.scrollBehavior = "smooth";
  };

  container.addEventListener("mouseup", stopDrag);
  container.addEventListener("mouseleave", stopDrag);

  // Cảm ứng
  let touchStartX = 0;
  let touchScrollLeft = 0;

  container.addEventListener("touchstart", (e) => {
    touchStartX = e.touches[0].pageX;
    touchScrollLeft = container.scrollLeft;
    container.style.scrollSnapType = "none";
    container.style.scrollBehavior = "auto";
  });

  container.addEventListener("touchmove", (e) => {
    const x = e.touches[0].pageX;
    const move = x - touchStartX;
    container.scrollLeft = touchScrollLeft - move;
  });

  container.addEventListener("touchend", () => {
    container.style.scrollSnapType = "x mandatory";
    container.style.scrollBehavior = "smooth";
  });
}

// Khởi tạo
document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll(".scroll-drag-container").forEach(container => {
    enableDragScroll(container);
  });
});
