function loadRecentTours() {
    fetch("/api/tours/recent", {
        credentials: "include"
    })
        .then(res => {
            if (!res.ok) throw new Error("HTTP " + res.status);
            return res.json();
        })
        .then(tours => {
            const container = document.getElementById("recent-tour-container");
            if (!container) return;

            container.innerHTML = "";

            if (!tours.length) {
                container.innerHTML = `<p class="text-muted">Bạn chưa xem tour nào.</p>`;
                return;
            }

            tours.forEach(t =>
                container.appendChild(createRecentTourCard(t))
            );
        })
        .catch(err => console.error("Recent tours error:", err));
}

function createRecentTourCard(tour) {
    const div = document.createElement("div");
    div.className = "tour-card";

    div.innerHTML = `
        <div style="position: relative;">
            <div class="badge-gift"><i class="fa-solid fa-gift"></i></div>
            <img src="${tour.duongDanAnhDaiDien}" class="tour-image">
        </div>

        <div class="tour-content">
            <h3 class="tour-title">${tour.tenTour}</h3>
            <div class="tour-rating">
                ${tour.soSaoTrungBinh ?? "0.0"} · Tuyệt vời | ${tour.soDanhGia ?? 0} đánh giá
            </div>

            <div class="tour-price">
                <span class="price-current">${Number(tour.gia).toLocaleString("vi-VN")} đ</span>
            </div>
        </div>
    `;

    div.onclick = () => (window.location.href = `/tour/${tour.maTour}`);

    return div;
}

function goToTour(maTour) {
    window.location.href = `/tour/${maTour}`;
}

