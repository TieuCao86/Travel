package com.example.travel.service;

import com.example.travel.dto.TourCardDTO;
import com.example.travel.dto.TourDetailDTO;
import com.example.travel.mapper.TourMapper;
import com.example.travel.model.Tour;
import com.example.travel.projection.TourCardProjection;
import com.example.travel.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private final TourMapper tourMapper;

    // Lấy tour (dùng cho danh sách & top-rated)
    public List<TourCardDTO> getTours(String tenTour,
                                      String loaiTour,
                                      BigDecimal minGia,
                                      BigDecimal maxGia,
                                      String sortBy,
                                      int offset,
                                      int limit) {

        List<TourCardProjection> projections = tourRepository.getTours(
                tenTour,
                loaiTour,
                minGia != null ? minGia.doubleValue() : null,
                maxGia != null ? maxGia.doubleValue() : null,
                sortBy,
                offset,
                limit
        );

        return tourMapper.toCardDTOList(projections);
    }

    // ✅ Lấy tour detail theo ID
    public Optional<TourDetailDTO> getTourById(Integer id) {
        return tourRepository.findById(id)
                .map(tourMapper::toDetailDTO);
    }

    // ✅ Thêm tour mới
    public Tour createTour(Tour tour) {
        return tourRepository.save(tour);
    }

    // ✅ Cập nhật tour
    public Tour updateTour(Integer id, Tour tour) {
        return tourRepository.findById(id)
                .map(existing -> {
                    existing.setTenTour(tour.getTenTour());
                    existing.setLoaiTour(tour.getLoaiTour());
                    existing.setMoTa(tour.getMoTa());
                    existing.setThoiGian(tour.getThoiGian());
                    // TODO: update thêm fields khác nếu cần
                    return tourRepository.save(existing);
                })
                .orElse(null);
    }

    // ✅ Xoá tour
    public boolean deleteTour(Integer id) {
        if (tourRepository.existsById(id)) {
            tourRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
