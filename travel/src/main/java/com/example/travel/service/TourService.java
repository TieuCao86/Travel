package com.example.travel.service;

import com.example.travel.dto.LichKhoiHanhDTO;
import com.example.travel.dto.TourCardDTO;
import com.example.travel.dto.TourDetailDTO;
import com.example.travel.mapper.TourMapper;
import com.example.travel.projection.TourCardProjection;
import com.example.travel.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private final TourMapper tourMapper;

    public List<TourCardDTO> getTopRatedTours(int top) {
        List<TourCardProjection> projections = tourRepository.getFullTourCards(
                null,
                null,
                null,
                null,
                null,
                0,
                5
        );
        return tourMapper.toCardDTOList(projections);
    }

    public List<TourCardDTO> searchTours(
            String tenTour,
            String loaiTour,
            String thanhPho,
            BigDecimal minGia,
            BigDecimal maxGia,
            int offset,
            int limit
    ) {
        List<TourCardProjection> projections = tourRepository.getFullTourCards(
                tenTour,
                loaiTour,
                thanhPho,
                minGia,
                maxGia,
                offset,
                limit
        );

        return tourMapper.toCardDTOList(projections);
    }

    public Optional<TourDetailDTO> getTourById(Integer id) {
        return tourRepository.findFullDetailById(id)
                .map(tourMapper::toDetailDTO) // map sang DTO trước
                .map(dto -> {
                    // Lọc duplicate theo ngày khởi hành
                    List<LichKhoiHanhDTO> distinctLich = dto.getLichKhoiHanhs().stream()
                            .collect(Collectors.toMap(
                                    LichKhoiHanhDTO::getNgayKhoiHanh,
                                    lich -> lich,
                                    (existing, replacement) -> existing // nếu trùng giữ bản đầu
                            ))
                            .values()
                            .stream()
                            .toList();
                    dto.setLichKhoiHanhs(distinctLich);
                    return dto;
                });
    }

}
