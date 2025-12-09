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
    private final NguoiDungRepository nguoiDungRepository;
    private final TourXemGanDayRepository tourXemGanDayRepository;

    // ==========================
    //  SECTION COMMON LOADER
    // ==========================
    private List<TourCardDTO> loadSectionTours(
            String loaiTour,
            String thanhPho,
            BigDecimal minGia,
            BigDecimal maxGia,
            int limit
    ) {
        List<TourCardProjection> projections =
                tourRepository.getFullTourCards(
                        null,           // tên tour
                        loaiTour,       // lọc theo loại
                        thanhPho,       // lọc theo thành phố
                        minGia,
                        maxGia,
                        "rating",
                        0,              // offset tại trang Home luôn là 0
                        limit
                );

        return tourMapper.toCardDTOList(projections);
    }

    // ==========================
    // SECTION REAL IMPLEMENT
    // ==========================

    // Section 1: Tour hot (5 SAO, GIÁ CAO → query tự ORDER đúng)
    public List<TourCardDTO> getHotTours() {
        return loadSectionTours(null, null, null, null, 6);
    }

    // Section 2: Tour miền Bắc
    public List<TourCardDTO> getNorthernTours() {
        return loadSectionTours(null, "Hà Nội", null, null, 6);
    }

    // Section 3: Tour nước ngoài
    public List<TourCardDTO> getInternationalTours() {
        return loadSectionTours("Quốc Tế", null, null, null, 6);
    }

    // Section 4: Tour giá rẻ
    public List<TourCardDTO> getCheapTours() {
        return loadSectionTours(null, null, null, new BigDecimal("3000000"), 6);
    }

    // ==========================
    // SEARCH TRANG /search
    // ==========================
    public List<TourCardDTO> searchTours(
            String tenTour,
            String loaiTour,
            String thanhPho,
            BigDecimal minGia,
            BigDecimal maxGia,
            String sort,
            int offset,
            int limit
    ) {
        return tourMapper.toCardDTOList(
                tourRepository.getFullTourCards(
                        tenTour,
                        loaiTour,
                        thanhPho,
                        minGia,
                        maxGia,
                        sort,
                        offset,
                        limit
                )
        );
    }

    // ==========================
    // GET BY ID
    // ==========================
    public Optional<TourDetailDTO> getTourById(Integer id) {
        return tourRepository.findFullDetailById(id)
                .map(tourMapper::toDetailDTO)
                .map(dto -> {
                    var distinct = dto.getLichKhoiHanhs().stream()
                            .collect(Collectors.toMap(
                                    LichKhoiHanhDTO::getNgayKhoiHanh,
                                    x -> x,
                                    (a, b) -> a
                            ))
                            .values().stream().toList();

                    dto.setLichKhoiHanhs(distinct);
                    return dto;
                });
    }

    // ==========================
    // RECENT TOURS
    // ==========================
    public List<TourCardDTO> getRecentTours(Integer maNguoiDung, String sessionId) {
        return tourMapper.toCardDTOList(
                tourRepository.findRecentTours(maNguoiDung, sessionId)
        );
    }

    // ==========================
    // SAVE RECENT VIEW
    // ==========================
    public void saveRecent(Integer tourId, Integer maNguoiDung, String sessionId) {
        if (maNguoiDung == null && (sessionId == null || sessionId.isBlank())) return;

        var existing = tourXemGanDayRepository.findExisting(tourId, maNguoiDung, sessionId);

        TourXemGanDay xem = existing.orElseGet(() -> {
            TourXemGanDay x = new TourXemGanDay();
            x.setTour(tourRepository.getReferenceById(tourId));
            if (maNguoiDung != null) {
                x.setNguoiDung(nguoiDungRepository.getReferenceById(maNguoiDung));
            } else {
                x.setSessionId(sessionId);
            }
            return x;
        });

        xem.setThoiGianXem(LocalDateTime.now());
        tourXemGanDayRepository.save(xem);
    }

    // ==========================
    // RELATED TOUR
    // ==========================
    public List<TourCardDTO> getRelatedTours(Integer tourId) {
        Tour tour = tourRepository.findById(tourId).orElse(null);
        if (tour == null) return List.of();

        String loaiTour = tour.getLoaiTour();
        String thanhPho = tour.getThanhPhos().isEmpty()
                ? null
                : tour.getThanhPhos().iterator().next().getTenThanhPho();

        List<TourCardProjection> result =
                tourRepository.findRelatedTours(
                        tourId,
                        loaiTour,
                        thanhPho,
                        tour.getThanhPhos().stream()
                                .findFirst()
                                .map(tp -> tp.getQuocGia().getTenQuocGia())
                                .orElse(null)
                );

        return tourMapper.toCardDTOList(result);
    }
}

