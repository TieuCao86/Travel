function enableDragScroll(container) {
  let isDragging = false;
  let startX = 0;
  let scrollLeft = 0;
  let moved = false;

  container.addEventListener("mousedown", (e) => {
    if (e.button !== 0) return;
    isDragging = true;
    moved = false;
    container.classList.add("dragging");
    container.style.cursor = "grabbing";
    container.style.scrollBehavior = "auto";

    startX = e.pageX;
    scrollLeft = container.scrollLeft;
  });

  container.addEventListener("mousemove", (e) => {
    if (!isDragging) return;
    const move = e.pageX - startX;
    if (Math.abs(move) > 5) moved = true; // chỉ tính là drag khi di chuyển đủ xa
    container.scrollLeft = scrollLeft - move;
  });

  const stopDrag = (e) => {
    if (!isDragging) return;
    isDragging = false;
    container.classList.remove("dragging");
    container.style.cursor = "grab";
    container.style.scrollBehavior = "smooth";

    if (moved) {
      // Nếu vừa kéo, chặn click trên link để không vô tình mở
      e.preventDefault();
    }
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
    moved = false;
  });

  container.addEventListener("touchmove", (e) => {
    const move = e.touches[0].pageX - touchStartX;
    if (Math.abs(move) > 5) moved = true;
    container.scrollLeft = touchScrollLeft - move;
  });

  container.addEventListener("touchend", (e) => {
    container.style.scrollBehavior = "smooth";
    if (moved) e.preventDefault();
  });
}
