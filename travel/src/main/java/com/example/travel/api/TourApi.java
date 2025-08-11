package com.example.travel.api;

import com.example.travel.dto.TourDTO;
import com.example.travel.mapper.TourMapper;
import com.example.travel.model.Tour;
import com.example.travel.service.TourService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tours")
@CrossOrigin(origins = "*")
public class TourApi {
    private final TourService tourService;

    public TourApi(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping
    public List<TourDTO> getAllTours() {
        return tourService.getAllTours().stream()
                .map(TourMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tour> getTourById(@PathVariable Integer id) {
        return tourService.getTourById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/top-rated")
    public List<TourDTO> getTopRatedTours() {
        return tourService.getTopTourWithHighestRating();
    }

    @PostMapping
    public Tour createTour(@RequestBody Tour tour) {
        return tourService.createTour(tour);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tour> updateTour(@PathVariable Integer id, @RequestBody Tour tour) {
        Tour updated = tourService.updateTour(id, tour);
        if (updated != null)
            return ResponseEntity.ok(updated);
        else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable Integer id) {
        if (tourService.deleteTour(id))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public List<TourDTO> searchTours(@RequestParam String q) {
        return tourService.searchTours(q).stream()
                .map(TourMapper::toDTO)
                .collect(Collectors.toList());
    }


}


