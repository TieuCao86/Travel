package com.example.travel.api;

import com.example.travel.dto.TourDTO;
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

    // ✅ Lấy danh sách tour có thể lọc, phân trang, sắp xếp
    @GetMapping
    public List<TourDTO> getTours(
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

    // ✅ Lấy tour theo ID (entity)
    @GetMapping("/{id}")
    public ResponseEntity<Tour> getTourById(@PathVariable Integer id) {
        return tourService.getTourById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Lấy top tour theo đánh giá
    @GetMapping("/top-rated")
    public List<TourDTO> getTopRatedTours(@RequestParam(defaultValue = "10") int limit) {
        return tourService.getTopTourWithHighestRating(limit);
    }

    // ✅ Tìm kiếm tour theo tên
    @GetMapping("/search")
    public List<TourDTO> searchTours(@RequestParam String q,
                                     @RequestParam(defaultValue = "0") int offset,
                                     @RequestParam(defaultValue = "10") int limit) {
        return tourService.searchTours(q, offset, limit);
    }

    // ✅ Thêm tour mới
    @PostMapping
    public Tour createTour(@RequestBody Tour tour) {
        return tourService.createTour(tour);
    }

    // ✅ Cập nhật tour
    @PutMapping("/{id}")
    public ResponseEntity<Tour> updateTour(@PathVariable Integer id, @RequestBody Tour tour) {
        Tour updated = tourService.updateTour(id, tour);
        if (updated != null)
            return ResponseEntity.ok(updated);
        else
            return ResponseEntity.notFound().build();
    }

    // ✅ Xóa tour
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable Integer id) {
        if (tourService.deleteTour(id))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }
}
