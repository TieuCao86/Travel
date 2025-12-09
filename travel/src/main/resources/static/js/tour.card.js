function createTourCard(tour) {
    const div = document.createElement("div");
    div.className = "resort-card mx-2 my-3";

    const giamGia = tour.giamGia ?? null;
    const giaGoc = Number(tour.gia) || 0;

    let giaSauGiam = giaGoc;
    let textGiamGia = null;
    let tietKiem = 0;

    if (giamGia !== null && giamGia !== undefined) {
        // Nếu là số
        if (typeof giamGia === "number") {

            // Nếu là giảm theo % (0.1 = 10%)
            if (giamGia > 0 && giamGia < 1) {
                const phanTram = giamGia * 100;
                giaSauGiam = Math.round(giaGoc * (1 - giamGia));
                tietKiem = giaGoc - giaSauGiam;
                textGiamGia = `${phanTram}%`;
            }

            // Giảm theo số tiền cụ thể
            else if (giamGia >= 1) {
                giaSauGiam = Math.max(giaGoc - giamGia, 0);
                tietKiem = giaGoc - giaSauGiam;
                textGiamGia = `${giamGia.toLocaleString("vi-VN")}₫`;
            }
        }

        // Nếu là chuỗi: "10%", "500000"
        else if (typeof giamGia === "string") {

            if (giamGia.includes("%")) {
                const phanTram = parseFloat(giamGia.replace("%", ""));
                if (!isNaN(phanTram)) {
                    giaSauGiam = Math.round(giaGoc * (1 - phanTram / 100));
                    tietKiem = giaGoc - giaSauGiam;
                    textGiamGia = `${phanTram}%`;
                }
            } else {
                const soTien = parseInt(giamGia.replace(/[^\d]/g, ""));
                if (!isNaN(soTien)) {
                    giaSauGiam = Math.max(giaGoc - soTien, 0);
                    tietKiem = soTien;
                    textGiamGia = `${soTien.toLocaleString("vi-VN")}₫`;
                }
            }
        }
    }

    // Biểu tượng phương tiện
    const transportIcons = getTransportIcons(tour.phuongTiens);
    const soSaoHienThi = Number(tour.soSaoTrungBinh ?? 0).toFixed(1);
    const soDanhGia = tour.soDanhGia ?? '100';

    // Build HTML đầy đủ
    div.innerHTML = `
        <a href="#" class="card-link" onclick="goToTour(${tour.maTour}); return false;">
            <div class="card-image-container">
                <img src="${tour.duongDanAnhDaiDien}" alt="Hình ảnh tour ${tour.tenTour}" class="card-image">

                <div class="heart-icon" onclick="event.stopPropagation(); toggleHeart(this)">
                    <i class="far fa-heart"></i>
                </div>

                <div class="transport-icons mt-1 ms-2 text-white" style="font-size: 1.1rem;">
                    ${transportIcons}
                </div>

                <div class="rating-badge" aria-label="Đánh giá ${soSaoHienThi} trên 5 từ ${soDanhGia} người dùng">
                    <span class="star">★</span>
                    <span class="rating-text">${soSaoHienThi}</span>
                    <span class="reviews-text">(${soDanhGia} đánh giá)</span>
                </div>

                ${textGiamGia ? `<div class="promotion-badge"><i class="fas fa-fire"></i><span>Giảm ${textGiamGia}!</span></div>` : ''}
            </div>

            <div class="card-content">
                <h3 class="card-title">
                    <i class="fas fa-map-marked-alt" style="color: #667eea; margin-right: 8px;"></i>
                    ${tour.tenTour}
                </h3>
                <div class="price-section">
                    ${tietKiem > 0 ? `<p class="original-price"><s>${giaGoc.toLocaleString('vi-VN')}₫</s></p>` : ''}
                    <p class="price-after-discount">${giaSauGiam.toLocaleString('vi-VN')}₫</p>
                    ${tietKiem > 0 ? `<p class="text-danger">Tiết kiệm ${tietKiem.toLocaleString('vi-VN')}₫</p>` : ''}
                </div>
            </div>
        </a>
    `;

    return div;
}

/**
 * getTransportIcons - trả về HTML icon phương tiện
 */
function getTransportIcons(phuongTiens) {
    if (!phuongTiens || phuongTiens.length === 0) return '';

    return phuongTiens.map(pt => {
        pt = pt.toLowerCase();
        let iconClass = 'fa-location-arrow'; // fallback
        if (pt.includes("xe khách") || pt.includes("bus")) iconClass = 'fa-bus';
        else if (pt.includes("máy bay") || pt.includes("flight") || pt.includes("plane")) iconClass = 'fa-plane-departure';
        else if (pt.includes("tàu hỏa") || pt.includes("train")) iconClass = 'fa-train';
        else if (pt.includes("tàu thủy") || pt.includes("ship") || pt.includes("tàu")) iconClass = 'fa-ship';
        else if (pt.includes("xe máy") || pt.includes("motor")) iconClass = 'fa-motorcycle';
        else if (pt.includes("ô tô") || pt.includes("car")) iconClass = 'fa-car';

        return `<div class="transport-icon-circle"><i class="fa-solid ${iconClass}"></i></div>`;
    }).join('');
}