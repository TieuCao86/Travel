let allTours = [];
let currentPage = 1;
const toursPerPage = 9;

function loadTours(apiUrl, targetSelector, usePagination = false) {
    fetch(apiUrl)
        .then(async response => {
                if (!response.ok) {
                    const errorText = await response.text();
                    throw new Error(`API lỗi: ${response.status} - ${errorText}`);
                }
                return response.json();
        })
        .then(tours => {
            allTours = tours;

            if (!tours.length) {
                document.querySelector(targetSelector).innerHTML = "<p class='text-danger'>Không có tour nào.</p>";
                return;
            }

            if (usePagination) {
                renderToursPage(currentPage, targetSelector);
                renderPagination(targetSelector);
            } else {
                // Home: hiển thị tất cả tour
                const container = document.querySelector(targetSelector);
                container.innerHTML = "";
                tours.forEach(tour => {
                    const card = createTourCard(tour);
                    container.appendChild(card);
                });
            }
        })
        .catch(error => console.error("Lỗi tải tour:", error));
}

/**
 * renderToursPage - hiển thị 1 trang tour (dùng khi phân trang)
 */
function renderToursPage(page, targetSelector) {
    const container = document.querySelector(targetSelector);
    container.innerHTML = "";

    const start = (page - 1) * toursPerPage;
    const end = start + toursPerPage;
    const pageTours = allTours.slice(start, end);

    if (!pageTours.length) {
        container.innerHTML = "<p class='text-danger'>Không có tour nào.</p>";
        return;
    }

    pageTours.forEach(tour => {
        const card = createTourCard(tour);
        container.appendChild(card);
    });
}

/**
 * renderPagination - tạo nút phân trang (dùng khi phân trang)
 */
function renderPagination(targetSelector) {
    const paginationContainer = document.querySelector(".pagination");
    if (!paginationContainer) return; // tránh lỗi nếu không có thẻ .pagination
    paginationContainer.innerHTML = "";

    const totalPages = Math.ceil(allTours.length / toursPerPage);

    // Previous
    paginationContainer.innerHTML += `
        <li class="page-item ${currentPage === 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${currentPage - 1}">&laquo;</a>
        </li>
    `;

    // Page numbers
    for (let i = 1; i <= totalPages; i++) {
        paginationContainer.innerHTML += `
            <li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" href="#" data-page="${i}">${i}</a>
            </li>
        `;
    }

    // Next
    paginationContainer.innerHTML += `
        <li class="page-item ${currentPage === totalPages ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${currentPage + 1}">&raquo;</a>
        </li>
    `;

    // Click event
    paginationContainer.querySelectorAll("a[data-page]").forEach(link => {
        link.addEventListener("click", function (e) {
            e.preventDefault();
            const page = parseInt(this.dataset.page);
            if (page > 0 && page <= totalPages) {
                currentPage = page;
                renderToursPage(currentPage, targetSelector);
                renderPagination(targetSelector);
            }
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
    const giaGoc = tour.gia;

    let giaSauGiam = giaGoc;
    let textGiamGia = null;
    let tietKiem = 0;

    // Xử lý giảm giá
    if (giamGia) {
        if (typeof giamGia === 'string' && giamGia.includes('%')) {
            const phanTram = parseFloat(giamGia.replace('%', ''));
            if (!isNaN(phanTram)) {
                giaSauGiam = Math.round(giaGoc * (1 - phanTram / 100));
                tietKiem = giaGoc - giaSauGiam;
                textGiamGia = `${phanTram}%`;
            }
        } else {
            const soTien = parseInt(giamGia.replace(/[^\d]/g, ""));
            if (!isNaN(soTien)) {
                giaSauGiam = Math.max(giaGoc - soTien, 0);
                tietKiem = giaGoc - giaSauGiam;
                textGiamGia = `${soTien.toLocaleString('vi-VN')}₫`;
            }
        }
    }

    // Biểu tượng phương tiện
    const transportIcons = getTransportIcons(tour.phuongTiens);
    const soSaoHienThi = Number(tour.soSaoTrungBinh ?? 0).toFixed(1);
    const soDanhGia = tour.soDanhGia ?? '100';

    // Build HTML đầy đủ
    div.innerHTML = `
        <a href="/tour/${tour.maTour}" class="card-link">
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
