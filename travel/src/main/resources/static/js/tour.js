const tourState = {};
let allTours = [];
let currentPage = 1;
const toursPerPage = 9;

async function loadTours(apiUrl, targetSelector, usePagination = false) {
    const container = document.querySelector(targetSelector);
    if (!container) {
        console.error("Không tìm thấy container:", targetSelector);
        return;
    }

    try {
        const response = await fetch(apiUrl);

        if (!response.ok) {
            const errorText = await response.text();
            container.innerHTML = `<p class='text-danger'>Lỗi API: ${response.status}</p>`;
            throw new Error(`API lỗi: ${response.status} - ${errorText}`);
        }

        const tours = await response.json();

        // Tạo state cho container nếu chưa có
        tourState[targetSelector] = {
            all: tours,
            page: 1
        };

        if (!tours.length) {
            container.innerHTML = "<p class='text-danger'>Không có tour nào.</p>";
            return;
        }

        if (usePagination) {
            renderToursPage(targetSelector);
            renderPagination(targetSelector);
        } else {
            // Home, top-rated: hiển thị hết
            container.innerHTML = "";
            tours.forEach(t => container.appendChild(createTourCard(t)));
        }

    } catch (err) {
        console.error("Lỗi tải tour:", err);
        container.innerHTML = `<p class='text-danger'>Không thể tải dữ liệu.</p>`;
    }
}

/**
 * renderToursPage - hiển thị 1 trang tour (dùng khi phân trang)
 */
function renderToursPage(targetSelector) {
    const state = tourState[targetSelector];
    const container = document.querySelector(targetSelector);

    const start = (state.page - 1) * toursPerPage;
    const end = start + toursPerPage;
    const pageTours = state.all.slice(start, end);

    container.innerHTML = "";
    pageTours.forEach(tour => container.appendChild(createTourCard(tour)));
}

/**
 * renderPagination - tạo nút phân trang (dùng khi phân trang)
 */
function renderPagination(targetSelector) {
    const pagination = document.querySelector(`${targetSelector}-pagination`);
    if (!pagination) return;

    const state = tourState[targetSelector];
    const totalPages = Math.ceil(state.all.length / toursPerPage);

    let html = "";

    for (let i = 1; i <= totalPages; i++) {
        html += `
            <li class="page-item ${i === state.page ? "active" : ""}">
                <a class="page-link" href="#" data-page="${i}">${i}</a>
            </li>`;
    }

    pagination.innerHTML = html;

    // gán sự kiện click
    pagination.querySelectorAll(".page-link").forEach(link => {
        link.addEventListener("click", e => {
            e.preventDefault();
            state.page = Number(link.dataset.page);
            renderToursPage(targetSelector);
            renderPagination(targetSelector);
        });
    });
}

/**
 * createTourCard - tạo card hiển thị tour (chung cho cả Home và trang phân trang)
 */
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

function loadRecentTours() {
    const userId = localStorage.getItem("userId");
    const maNguoiDungParam = userId ? `maNguoiDung=${userId}&` : "";

    const clientId = getSessionId();

    const apiUrl = `/api/tours/recent?${maNguoiDungParam}sessionId=${clientId}`;

    fetch(apiUrl)
        .then(res => res.json())
        .then(tours => {
            const container = document.getElementById("recent-tour-container");
            container.innerHTML = "";

            if (!tours.length) {
                container.innerHTML = `<p class="text-muted">Bạn chưa xem tour nào.</p>`;
                return;
            }

            tours.forEach(tour => {
                container.appendChild(createRecentTourCard(tour));
            });
        })
        .catch(err => console.error("Lỗi load recent tours:", err));
}

function getSessionId() {
    let clientId = localStorage.getItem("clientId");
    if (!clientId) {
        clientId = self.crypto.randomUUID();
        localStorage.setItem("clientId", clientId);
    }
    return clientId;
}

function goToTour(maTour) {
    window.location.href = `/tour/${maTour}?clientId=${getSessionId()}`;
}

function createRecentTourCard(tour) {
    const div = document.createElement("div");
    div.className = "tour-card";

    div.innerHTML = `
        <div style="position: relative;">
            <div class="badge-gift"><i class="fa-solid fa-gift"></i></div>
            <img src="${tour.duongDanAnhDaiDien}" alt="${tour.tenTour}" class="tour-image">
        </div>

        <div class="tour-content">
            <button class="close-btn" onclick="event.stopPropagation(); removeRecentTour(${tour.maTour})">
                ✕
            </button>

            <div>
                <h3 class="tour-title">${tour.tenTour}</h3>
                <div class="tour-rating">
                    ${tour.soSaoTrungBinh ?? "0.0"} · Tuyệt vời | ${tour.soDanhGia ?? 0} đánh giá
                </div>
            </div>

            <div class="tour-price">
                <span class="price-current">${Number(tour.gia).toLocaleString("vi-VN")} đ</span>
            </div>
        </div>
    `;

    div.addEventListener("click", () => {
        window.location.href = `/tour/${tour.maTour}`;
    });

    return div;
}

function loadRelatedTours(tourId) {
    const url = `/api/tours/related/${tourId}`;
    loadTours(url, "#related-tour-container", false);
}






