document.addEventListener("DOMContentLoaded", function() {

    // --------- Expand / Collapse ALL Schedule ---------
    const toggleBtn = document.getElementById("xemTatCa");
    const allCollapses = document.querySelectorAll("#tourSchedule .accordion-collapse");
    let expanded = false;

    if (toggleBtn) {
        toggleBtn.addEventListener("click", function () {
            allCollapses.forEach(collapse => {
                const bsCollapse = new bootstrap.Collapse(collapse, { toggle: false });
                expanded ? bsCollapse.hide() : bsCollapse.show();
            });

            expanded = !expanded;
            toggleBtn.textContent = expanded ? "Rút gọn" : "Xem tất cả";
        });
    }

    // --------- Load Related Tours ---------
    const input = document.getElementById("tour-id");
    if (input) {
        const tourId = input.value;
        saveRecentTour(tourId);
        loadRelatedTours(tourId);
    }
});

function loadRelatedTours(tourId) {
    const url = `/api/tours/related/${tourId}`;
    loadTours(url, "#related-tour-container", false);
}

function saveRecentTour(tourId) {
    fetch(`/api/tours/recent/${tourId}`, {
        method: "POST",
        credentials: "include" // QUAN TRỌNG: để gửi JSESSIONID
    }).catch(err => console.error(err));
}
