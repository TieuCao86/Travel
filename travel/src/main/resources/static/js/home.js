document.addEventListener("DOMContentLoaded", function () {
    loadTours("/api/tours/top-rated", "#top-tour-container", false);

    const filterButtons = document.querySelectorAll(".filter-btn");
    const searchBox = document.querySelector(".search-box");
    const searchBtn = document.querySelector(".search-section .btn-warning");

    // Lấy loại search từ nút active ban đầu
    let selectedType = getTypeFromText(document.querySelector(".filter-btn.active").textContent.trim());

    // Khi click nút filter → chỉ đổi loại
    filterButtons.forEach(btn => {
        btn.addEventListener("click", function () {
            filterButtons.forEach(b => b.classList.remove("active"));
            this.classList.add("active");

            selectedType = getTypeFromText(this.textContent.trim());
        });
    });

    // Khi bấm search → chuyển qua trang mới
    searchBtn.addEventListener("click", function () {
        const keyword = searchBox.value.trim();
        if (!keyword) {
            alert("Vui lòng nhập từ khóa tìm kiếm");
            return;
        }

        const activeBtn = document.querySelector(".filter-btn.active");
        const selectedType = getTypeFromText(activeBtn.textContent.trim());

        window.location.href = `/search/${selectedType}?q=${encodeURIComponent(keyword)}`;
    });

    // Hàm chuyển text sang type
    function getTypeFromText(text) {
        switch (text) {
            case "Khách sạn": return "hotel";
            case "Tours": return "tour";
            case "Vé": return "ticket";
            case "Hoạt động": return "activity";
            default: return "tour"; // fallback
        }
    }
});
