document.addEventListener("DOMContentLoaded", function () {
    const params = new URLSearchParams(window.location.search);
    const searchKeyword = params.get("q");

    if (searchKeyword) {
        loadTours(`/api/tours/search?q=${encodeURIComponent(searchKeyword)}`, "#search-result-container");
    }
});
