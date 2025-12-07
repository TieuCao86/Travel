package com.example.travel.service;

import com.example.travel.dto.LichKhoiHanhDTO;
import com.example.travel.dto.TourCardDTO;
import com.example.travel.dto.TourDetailDTO;
import com.example.travel.mapper.TourMapper;
import com.example.travel.model.NguoiDung;
import com.example.travel.model.ThanhPho;
import com.example.travel.model.Tour;
import com.example.travel.model.TourXemGanDay;
import com.example.travel.projection.TourCardProjection;
import com.example.travel.repository.NguoiDungRepository;
import com.example.travel.repository.TourRepository;
import com.example.travel.repository.TourXemGanDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private final TourMapper tourMapper;
    private  final NguoiDungRepository nguoiDungRepository;
    private final TourXemGanDayRepository tourXemGanDayRepository;

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

    public List<TourCardDTO> getRecentTours(Integer maNguoiDung, String sessionId) {

        List<TourCardProjection> projections =
                tourRepository.findRecentTours(maNguoiDung, sessionId);

        return tourMapper.toCardDTOList(projections);
    }

    public void saveRecent(Integer tourId, Integer maNguoiDung, String sessionId) {

        if (maNguoiDung == null && (sessionId == null || sessionId.isBlank())) {
            return;
        }

        Optional<TourXemGanDay> existing =
                tourXemGanDayRepository.findExisting(tourId, maNguoiDung, sessionId);

        TourXemGanDay xem;

        if (existing.isPresent()) {
            xem = existing.get();
            xem.setThoiGianXem(LocalDateTime.now());
        } else {
            xem = new TourXemGanDay();
            xem.setTour(tourRepository.getReferenceById(tourId));
            xem.setThoiGianXem(LocalDateTime.now());

            if (maNguoiDung != null) {
                xem.setNguoiDung(nguoiDungRepository.getReferenceById(maNguoiDung));
            } else {
                xem.setSessionId(sessionId);
            }
        }

        tourXemGanDayRepository.save(xem);
    }

    public List<TourCardDTO> getRelatedTours(Integer tourId) {
        Tour tour = tourRepository.findById(tourId).orElse(null);
        if (tour == null) return List.of();

        // Lấy loaiTour và thành phố/quốc gia
        String loaiTour = tour.getLoaiTour();
        String tenThanhPho = null;
        String tenQuocGia = null;

        if (!tour.getThanhPhos().isEmpty()) {
            ThanhPho tp = tour.getThanhPhos().iterator().next(); // vì là Set
            tenThanhPho = tp.getTenThanhPho();
            tenQuocGia = tp.getQuocGia().getTenQuocGia();
        }

        List<TourCardProjection> result =
                tourRepository.findRelatedTours(tourId, loaiTour, tenThanhPho, tenQuocGia);

        return result.stream().map(r ->
                TourCardDTO.builder()
                        .maTour(r.getMaTour())
                        .tenTour(r.getTenTour())
                        .loaiTour(r.getLoaiTour())
                        .moTa(r.getMoTa())
                        .thoiGian(r.getThoiGian())
                        .duongDanAnhDaiDien(r.getDuongDanAnhDaiDien())
                        .soSaoTrungBinh(r.getSoSaoTrungBinh())
                        .soDanhGia(r.getSoDanhGia())
                        .gia(r.getGia())
                        .build()
        ).toList();
    }


}
