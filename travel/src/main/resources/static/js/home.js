document.addEventListener("DOMContentLoaded", function () {

    loadRecentTours?.();
    loadTours?.("/api/tours/top-rated", "#top-tour-container", false);
    loadTours?.("/api/tours/international", "#tour-foreign", false);

    const filterButtons = document.querySelectorAll(".filter-btn");
    const searchBox = document.querySelector(".search-box");
    const searchBtn = document.querySelector(".search-section .btn-warning");

    let selectedType = "tour";
    const activeFilterBtn = document.querySelector(".filter-btn.active");
    if (activeFilterBtn) {
        selectedType = getTypeFromText(activeFilterBtn.textContent.trim());
    }

    filterButtons.forEach(btn => {
        btn.addEventListener("click", function () {
            filterButtons.forEach(b => b.classList.remove("active"));
            this.classList.add("active");
            selectedType = getTypeFromText(this.textContent.trim());
        });
    });

    if (searchBtn && searchBox) {
        searchBtn.addEventListener("click", function () {
            const keyword = searchBox.value.trim();
            if (!keyword) {
                alert("Vui lòng nhập từ khóa tìm kiếm");
                return;
            }
            window.location.href = `/search/${selectedType}?q=${encodeURIComponent(keyword)}`;
        });
    }

    function getTypeFromText(text) {
        switch (text) {
            case "Khách sạn": return "hotel";
            case "Tours": return "tour";
            case "Vé": return "ticket";
            case "Hoạt động": return "activity";
            default: return "tour";
        }
    }
});
