document.addEventListener("DOMContentLoaded", function () {
    const params = new URLSearchParams(window.location.search);

    // Lấy tất cả tham số search gửi từ trang home
    const tenTour = params.get("tenTour");
    const loaiTour = params.get("loaiTour");
    const thanhPho = params.get("thanhPho");
    const minGia = params.get("minGia");
    const maxGia = params.get("maxGia");

    // Ghép query đúng với API /api/tours/search
    const query = new URLSearchParams();

    if (tenTour) query.append("tenTour", tenTour);
    if (loaiTour) query.append("loaiTour", loaiTour);
    if (thanhPho) query.append("thanhPho", thanhPho);
    if (minGia) query.append("minGia", minGia);
    if (maxGia) query.append("maxGia", maxGia);

    // Nếu không có bất kỳ filter nào → không gọi API
    if ([tenTour, loaiTour, thanhPho, minGia, maxGia].every(v => !v)) {
        console.log("No search parameters found");
        return;
    }

    // Gọi API
    loadTours(
        `/api/tours/search?${query.toString()}`,
        "#search-result-container",
        true, // bật phân trang
        function(tours) {
            document.querySelector("#total-tours").textContent = tours.length;
        }
    );
});
