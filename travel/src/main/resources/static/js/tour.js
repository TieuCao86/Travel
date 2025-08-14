function loadTours(apiUrl, targetSelector, onLoaded) {
    fetch(apiUrl)
        .then(response => {
            if (!response.ok) throw new Error("API lỗi");
            return response.json();
        })
        .then(tours => {
            const container = document.querySelector(targetSelector);
            container.innerHTML = ""; // Xóa dữ liệu cũ

            if (!tours.length) {
                container.innerHTML = "<p class='text-danger'>Không có tour nào.</p>";
            } else {
                tours.forEach(tour => {
                    const card = createTourCard(tour);
                    container.appendChild(card);
                });
            }

            // Nếu có callback thì gọi
            if (typeof onLoaded === "function") {
                onLoaded(tours);
            }
        })
        .catch(error => {
            console.error("Lỗi tải tour:", error);
        });
}

function createTourCard(tour) {
    const div = document.createElement("div");
    div.className = "resort-card mx-2 my-3";

    const giamGia = tour.giamGia ?? null;
    const giaGoc = tour.gia;

    // Xác định kiểu giảm giá: phần trăm hay tiền
    let giaSauGiam = giaGoc;
    let laPhanTram = false;
    let textGiamGia = null;
    let tietKiem = 0;

    if (giamGia) {
        if (typeof giamGia === 'string' && giamGia.includes('%')) {
            laPhanTram = true;
            const phanTram = parseFloat(giamGia.replace('%', ''));
            if (!isNaN(phanTram)) {
                giaSauGiam = Math.round(giaGoc * (1 - phanTram / 100));
                tietKiem = giaGoc - giaSauGiam;
                textGiamGia = `${phanTram}%`;
            }
        } else {
            const soTien = parseInt(giamGia);
            if (!isNaN(soTien)) {
                giaSauGiam = Math.max(giaGoc - soTien, 0);
                tietKiem = giaGoc - giaSauGiam;
                textGiamGia = `${soTien.toLocaleString('vi-VN')}₫`;
            }
        }
    }

    const transportIcons = getTransportIcons(tour.phuongTiens);
    const soSaoHienThi = Number(tour.soSaoTrungBinh ?? 0).toFixed(1);
    const soDanhGia = tour.soDanhGia ?? '100';

    div.innerHTML = `
        <div class="card-image-container">
            <img src="${tour.duongDanAnhDaiDien}" alt="${tour.tenTour}" class="card-image">

            <div class="heart-icon" onclick="toggleHeart(this)">
                <i class="far fa-heart"></i>
            </div>

            <div class="transport-icons mt-1 ms-2 text-white" style="font-size: 1.1rem;">
                ${transportIcons}
            </div>

            <div class="rating-badge">
                <span class="star">★</span>
                <span class="rating-text">${soSaoHienThi}</span>
                <span class="reviews-text">(${soDanhGia} đánh giá)</span>
            </div>

            ${textGiamGia ? `
            <div class="promotion-badge">
                <i class="fas fa-fire"></i>
                <span>Giảm ${textGiamGia}!</span>
            </div>` : ''}
        </div>

        <div class="card-content">
            <h3 class="card-title">
                <i class="fas fa-map-marked-alt" style="color: #667eea; margin-right: 8px;"></i>
                ${tour.tenTour}
            </h3>

            <div class="card-subtitle">
                <div class="duration">
                    <i class="far fa-calendar-alt" style="color: #667eea;"></i>
                    <span>${tour.thoiGian}</span>
                </div>
                <div class="category">
                    <i class="fas fa-tags" style="color: #667eea;"></i>
                    <span>${tour.loaiTour}</span>
                </div>
            </div>

            <div class="divider"></div>

            <div class="price-section">
                <div class="price-container">
                    ${textGiamGia ? `<div class="original-price"><s>${new Intl.NumberFormat('vi-VN').format(giaGoc)} VND</s></div>` : ''}
                    <div>
                        <span class="price">${new Intl.NumberFormat('vi-VN').format(giaSauGiam)} VND</span>
                        <span class="price-unit">/ người</span>
                    </div>
                    ${textGiamGia ? `<div class="savings-text">Tiết kiệm: ${new Intl.NumberFormat('vi-VN').format(tietKiem)} VND</div>` : ''}
                </div>
                <button class="btn book-btn" onclick="bookNow()">Đặt Tour</button>
            </div>
        </div>
    `;

    return div;
}

function getTransportIcons(phuongTiens) {
    if (!phuongTiens || phuongTiens.length === 0) return '';

    return phuongTiens.map(pt => {
        pt = pt.toLowerCase();
        let iconClass = 'fa-location-arrow'; // fallback
        if (pt.includes("xe khách") || pt.includes("bus")) {
            iconClass = 'fa-bus';
        } else if (pt.includes("máy bay") || pt.includes("flight") || pt.includes("plane")) {
            iconClass = 'fa-plane-departure';
        } else if (pt.includes("tàu hỏa") || pt.includes("train")) {
            iconClass = 'fa-train';
        } else if (pt.includes("tàu thủy") || pt.includes("ship") || pt.includes("tàu")) {
            iconClass = 'fa-ship';
        } else if (pt.includes("xe máy") || pt.includes("motor")) {
            iconClass = 'fa-motorcycle';
        } else if (pt.includes("ô tô") || pt.includes("car")) {
            iconClass = 'fa-car';
        }

        return `<div class="transport-icon-circle"><i class="fa-solid ${iconClass}"></i></div>`;
    }).join('');
}

function scrollTours(direction) {
    const container = document.getElementById('top-tour-container');
    const card = container.querySelector('.resort-card');
    if (card) {
        const scrollStep = card.offsetWidth + 20; // cộng khoảng cách giữa các card
        container.scrollBy({
            left: direction * scrollStep,
            behavior: 'smooth'
        });
    }
}



