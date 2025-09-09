package com.example.travel.api;

import com.example.travel.dto.TourCardDTO;
import com.example.travel.dto.TourDetailDTO;
import com.example.travel.model.Tour;
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

    // ✅ Lấy Top Rated Tours
    @GetMapping("/top-rated")
    public ResponseEntity<List<TourCardDTO>> getTopRatedTours(
            @RequestParam(defaultValue = "5") int top) {

        List<TourCardDTO> tours = tourService.getTours(
                null,
                null,
                null,
                null,
                "rating",
                0,
                top
        );

        return ResponseEntity.ok(tours);
    }

    // ✅ Danh sách tours (có filter + sort + paging)
    @GetMapping
    public List<TourCardDTO> getTours(
            @RequestParam(required = false) String tenTour,
            @RequestParam(required = false) String loaiTour,
            @RequestParam(required = false) BigDecimal minGia,
            @RequestParam(required = false) BigDecimal maxGia,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return tourService.getTours(tenTour, loaiTour, minGia, maxGia, sortBy, offset, limit);
    }

    // ✅ Lấy chi tiết tour theo ID
    @GetMapping("/{id}")
    public ResponseEntity<TourDetailDTO> getTourById(@PathVariable Integer id) {
        return tourService.getTourById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Tạo tour mới
    @PostMapping
    public ResponseEntity<Tour> createTour(@RequestBody Tour tour) {
        return ResponseEntity.ok(tourService.createTour(tour));
    }

    // ✅ Cập nhật tour
    @PutMapping("/{id}")
    public ResponseEntity<Tour> updateTour(@PathVariable Integer id, @RequestBody Tour tour) {
        Tour updated = tourService.updateTour(id, tour);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // ✅ Xoá tour
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable Integer id) {
        return tourService.deleteTour(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
