const tourState = {};
const toursPerPage = 9;

async function loadTours(apiUrl, targetSelector, usePagination = false) {
    const container = document.querySelector(targetSelector);
    if (!container) return;

    try {
        const res = await fetch(apiUrl);
        if (!res.ok) throw new Error("API lỗi");

        const tours = await res.json();

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
            container.innerHTML = "";
            tours.forEach(t => container.appendChild(createTourCard(t)));
        }
    } catch (err) {
        container.innerHTML = "<p class='text-danger'>Không thể tải dữ liệu.</p>";
        console.error(err);
    }
}

function renderToursPage(targetSelector) {
    const state = tourState[targetSelector];
    const container = document.querySelector(targetSelector);

    const start = (state.page - 1) * toursPerPage;
    const end = start + toursPerPage;

    container.innerHTML = "";
    state.all.slice(start, end)
        .forEach(tour => container.appendChild(createTourCard(tour)));
}

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

    pagination.querySelectorAll(".page-link").forEach(link => {
        link.addEventListener("click", e => {
            e.preventDefault();
            state.page = Number(link.dataset.page);
            renderToursPage(targetSelector);
            renderPagination(targetSelector);
        });
    });
}
