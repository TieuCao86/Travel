package com.example.travel.api;

import com.example.travel.dto.TourCardDTO;
import com.example.travel.dto.TourDetailDTO;
import com.example.travel.model.Tour;
import com.example.travel.projection.TourCardProjection;
import com.example.travel.service.TourService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/tours")
@CrossOrigin(origins = "*")
public class TourApi {

    private final TourService tourService;

    public TourApi(TourService tourService) {
        this.tourService = tourService;
    }

    //API SEARCH (Specification + pagination + sort)
    @GetMapping("/search")
    public ResponseEntity<List<TourCardDTO>> searchTours(
            @RequestParam(required = false) String tenTour,
            @RequestParam(required = false) String loaiTour,
            @RequestParam(required = false) String thanhPho,
            @RequestParam(required = false) BigDecimal minGia,
            @RequestParam(required = false) BigDecimal maxGia,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<TourCardDTO> tours = tourService.searchTours(
                tenTour, loaiTour, thanhPho, minGia, maxGia, sortBy, offset, limit
        );
        return ResponseEntity.ok(tours);
    }

    // TOP-RATED TOUR (giữ nguyên nhưng dùng searchTours)
    @GetMapping("/top-rated")
    public ResponseEntity<List<TourCardDTO>> getTopRatedTours() {
        return ResponseEntity.ok(tourService.getHotTours());
    }

    // Chi tiết tour
    @GetMapping("/{id}")
    public ResponseEntity<TourDetailDTO> getTourById(@PathVariable Integer id) {
        return tourService.getTourById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/view")
    public void saveView(
            @RequestParam Integer maTour,
            @RequestParam(required = false) Integer maNguoiDung,
            @RequestParam(required = false) String sessionId
    ) {
        tourService.saveRecent(maTour, maNguoiDung, sessionId);
    }

    @GetMapping("/recent")
    public List<TourCardDTO> getRecent(
            @RequestParam(required = false) Integer maNguoiDung,
            @RequestParam(required = false) String sessionId
    ) {
        return tourService.getRecentTours(maNguoiDung, sessionId);
    }

    @GetMapping("/related/{tourId}")
    public List<TourCardDTO> getRelatedTours(@PathVariable Integer tourId) {
        return tourService.getRelatedTours(tourId);
    }

    @GetMapping("/international")
    public List<TourCardDTO> getInternational() {
        return tourService.getInternationalTours();
    }

}
