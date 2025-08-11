package com.example.travel.service;

import com.example.travel.dto.TourDTO;
import com.example.travel.mapper.TourMapper;
import com.example.travel.model.Tour;
import com.example.travel.repository.TourRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TourService {
    private final TourRepository tourRepository;

    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public Optional<Tour> getTourById(Integer id) {
        return tourRepository.findById(id);
    }

    public Tour createTour(Tour tour) {
        return tourRepository.save(tour);
    }

    public Tour updateTour(Integer id, Tour updatedTour) {
        return tourRepository.findById(id)
                .map(tour -> {
                    tour.setTenTour(updatedTour.getTenTour());
                    tour.setLoaiTour(updatedTour.getLoaiTour());
                    tour.setMoTa(updatedTour.getMoTa());
                    tour.setThoiGian(updatedTour.getThoiGian());
                    return tourRepository.save(tour);
                }).orElse(null);
    }

    public boolean deleteTour(Integer id) {
        if (tourRepository.existsById(id)) {
            tourRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<TourDTO> getTopTourWithHighestRating() {
        return tourRepository.findTopTourWithHighestRating().stream()
                .map(obj -> {
                    TourDTO dto = new TourDTO();
                    dto.setMaTour((Integer) obj[0]);
                    dto.setTenTour((String) obj[1]);
                    dto.setLoaiTour((String) obj[2]);
                    dto.setMoTa((String) obj[3]);
                    dto.setThoiGian((String) obj[4]);
                    dto.setGia((BigDecimal) obj[5]);

                    dto.setSoSaoTrungBinh(obj[6] != null ? ((Double) obj[6]) : 0.0);
                    dto.setSoDanhGia(obj[7] != null ? ((Integer) obj[7]) : 0);
                    dto.setDuongDanAnhDaiDien((String) obj[8]);

                    String phuongTienStr = (String) obj[9];
                    dto.setPhuongTiens(phuongTienStr != null ? List.of(phuongTienStr.split(",\\s*")) : List.of());

                    Boolean laPhanTram = obj[10] != null && (Boolean) obj[10];
                    BigDecimal giaTri = (BigDecimal) obj[11];
                    if (giaTri != null) {
                        if (laPhanTram) {
                            dto.setGiamGia(giaTri.intValue() + "%");
                        } else {
                            dto.setGiamGia(giaTri.toString());
                        }
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<Tour> searchTours(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return tourRepository.findAll();
        }
        return tourRepository.findByTenTourContainingIgnoreCase(keyword);
    }


}

