document.addEventListener("DOMContentLoaded", function () {
    const params = new URLSearchParams(window.location.search);
    const searchKeyword = params.get("q");

    if (searchKeyword) {
        // Bật phân trang bằng true
        loadTours(
            `/api/tours/search?q=${encodeURIComponent(searchKeyword)}`,
            "#search-result-container",
            true, // usePagination = true
            function(tours) {
                document.querySelector("#total-tours").textContent = tours.length;
            }
        );
    }
});
