package com.example.travel.api;

import com.example.travel.dto.TourCardDTO;
import com.example.travel.dto.TourDetailDTO;
import com.example.travel.service.TourService;
import com.example.travel.service.TourXemGanDayService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tours")
@CrossOrigin(
        origins = "http://localhost:8080",
        allowCredentials = "true"
)
public class TourApi {

    private final TourService tourService;
    private final TourXemGanDayService tourXemGanDayService;

    public TourApi(
            TourService tourService,
            TourXemGanDayService tourXemGanDayService
    ) {
        this.tourService = tourService;
        this.tourXemGanDayService = tourXemGanDayService;
    }

    /* =====================================================
     * HOME PAGE
     * ===================================================== */

    @GetMapping("/top-rated")
    public List<TourCardDTO> getHotTours() {
        return tourService.getHotTours();
    }

    @GetMapping("/international")
    public List<TourCardDTO> getInternationalTours() {
        return tourService.getInternationalTours();
    }

    @GetMapping("/cheap")
    public List<TourCardDTO> getCheapTours() {
        return tourService.getCheapTours();
    }

    /* =====================================================
     * SEARCH
     * ===================================================== */

    @GetMapping("/search")
    public ResponseEntity<List<TourCardDTO>> searchTours(
            @RequestParam(required = false) String tenTour,
            @RequestParam(required = false) String loaiTour,
            @RequestParam(required = false) BigDecimal minGia,
            @RequestParam(required = false) BigDecimal maxGia,
            @RequestParam(defaultValue = "rating") String sort,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "12") int limit
    ) {
        return ResponseEntity.ok(
                tourService.searchTours(
                        tenTour,
                        loaiTour,
                        minGia,
                        maxGia,
                        sort,
                        offset,
                        limit
                )
        );
    }

    @GetMapping("/recent")
    public List<TourCardDTO> getRecentTours(
            @CookieValue(value = "CLIENT_ID", required = false) String clientId,
            HttpSession session,
            HttpServletResponse response
    ) {
        if (clientId == null) {
            clientId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("CLIENT_ID", clientId);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(cookie);
        }

        Integer userId = (Integer) session.getAttribute("USER_ID");
        return tourService.getRecentTours(userId, clientId);
    }

    /* =====================================================
     * TOUR DETAIL
     * ===================================================== */

    @GetMapping("/{id}")
    public ResponseEntity<TourDetailDTO> getTourDetail(@PathVariable Integer id) {
        return tourService.getTourDetail(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/recent/{tourId}")
    public ResponseEntity<Void> saveRecentTour(
            @PathVariable Integer tourId,
            @CookieValue(value = "CLIENT_ID", required = false) String clientId,
            HttpServletResponse response,
            HttpSession session
    ) {
        if (clientId == null) {
            clientId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("CLIENT_ID", clientId);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(cookie);
        }

        Integer userId = (Integer) session.getAttribute("USER_ID");
        tourXemGanDayService.save(userId, clientId, tourId);
        return ResponseEntity.ok().build();
    }

}
