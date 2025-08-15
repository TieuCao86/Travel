let allTours = [];
let currentPage = 1;
const toursPerPage = 9;

function loadTours(apiUrl, targetSelector, onLoaded) {
    fetch(apiUrl)
        .then(response => {
            if (!response.ok) throw new Error("API lỗi");
            return response.json();
        })
        .then(tours => {
            allTours = tours;
            if (typeof onLoaded === "function") {
                onLoaded(tours);
            }
            renderToursPage(currentPage, targetSelector);
            renderPagination(targetSelector);
        })
        .catch(error => {
            console.error("Lỗi tải tour:", error);
        });
}

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

function renderPagination(targetSelector) {
    const paginationContainer = document.querySelector(".pagination");
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
