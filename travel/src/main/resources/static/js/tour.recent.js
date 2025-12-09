function getSessionId() {
    let clientId = localStorage.getItem("clientId");
    if (!clientId) {
        clientId = self.crypto.randomUUID();
        localStorage.setItem("clientId", clientId);
    }
    return clientId;
}

function loadRecentTours() {
    const userId = localStorage.getItem("userId") ?? "";
    const clientId = getSessionId();

    const apiUrl = `/api/tours/recent?maNguoiDung=${userId}&sessionId=${clientId}`;

    fetch(apiUrl)
        .then(res => res.json())
        .then(tours => {
            const container = document.getElementById("recent-tour-container");
            container.innerHTML = "";

            if (!tours.length) {
                container.innerHTML = `<p class="text-muted">Bạn chưa xem tour nào.</p>`;
                return;
            }

            tours.forEach(t => container.appendChild(createRecentTourCard(t)));
        })
        .catch(err => console.error(err));
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
    window.location.href = `/tour/${maTour}?clientId=${getSessionId()}`;
}
