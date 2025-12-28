package com.example.travel.service;

import com.example.travel.dto.LichTrinhChiTietDTO;
import com.example.travel.dto.TourCardDTO;
import com.example.travel.dto.TourDetailDTO;
import com.example.travel.mapper.DanhGiaMapper;
import com.example.travel.mapper.TourMapper;
import com.example.travel.projection.TourCardProjection;
import com.example.travel.repository.DanhGiaRepository;
import com.example.travel.repository.LichTrinhChiTietRepository;
import com.example.travel.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private final DanhGiaRepository danhGiaRepository;
    private final TourMapper tourMapper;
    private final DanhGiaMapper danhGiaMapper;
    private final LichTrinhChiTietRepository lichTrinhChiTietRepository;

    /* =====================================================
     * HOME PAGE – 1 QUERY
     * ===================================================== */

    private List<TourCardDTO> loadHomeSection(
            String loaiTour,
            BigDecimal minGia,
            BigDecimal maxGia,
            int limit
    ) {
        List<TourCardProjection> projections =
                tourRepository.getHomeTours(
                        null,           // tenTour
                        loaiTour,
                        minGia,
                        maxGia,
                        "rating",
                        0,
                        limit
                );

        return tourMapper.toCardDTOList(projections);
    }

    public List<TourCardDTO> getHotTours() {
        return loadHomeSection(null, null, null, 6);
    }

    public List<TourCardDTO> getInternationalTours() {
        return loadHomeSection("Quốc Tế", null, null, 6);
    }

    public List<TourCardDTO> getCheapTours() {
        return loadHomeSection(null, null, new BigDecimal("3000000"), 6);
    }

    public List<TourCardDTO> getRecentTours(Integer maNguoiDung, String sessionId) {
        return tourMapper.toCardDTOList(
                tourRepository.getRecentTours(maNguoiDung, sessionId)
        );
    }

    /* =====================================================
     * SEARCH PAGE
     * ===================================================== */

    public List<TourCardDTO> searchTours(
            String tenTour,
            String loaiTour,
            BigDecimal minGia,
            BigDecimal maxGia,
            String sort,
            int offset,
            int limit
    ) {
        return tourMapper.toCardDTOList(
                tourRepository.getHomeTours(
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

    /* =====================================================
     * TOUR DETAIL – 1 QUERY
     * ===================================================== */

    public Optional<TourDetailDTO> getTourDetail(Integer maTour) {

        Optional<TourDetailDTO> opt =
                tourRepository.getTourDetail(maTour)
                        .map(tourMapper::toDetailDTO);

        opt.ifPresent(dto -> {

            // Đánh giá – OK
            dto.setDanhGiaList(
                    danhGiaMapper.toDTOList(
                            danhGiaRepository.findDanhGiaByTour(
                                    maTour,
                                    PageRequest.of(0, 5)
                            )
                    )
            );

            List<LichTrinhChiTietDTO> hoatDongList =
                    lichTrinhChiTietRepository.findByTour(maTour);

            Map<Integer, List<LichTrinhChiTietDTO>> map =
                    hoatDongList.stream()
                            .collect(Collectors.groupingBy(
                                    LichTrinhChiTietDTO::getMaNgay
                            ));

            dto.getLichTrinhNgayList().forEach(ngay ->
                    ngay.setHoatDongList(
                            map.getOrDefault(ngay.getMaNgay(), List.of())
                    )
            );
        });

        return opt;
    }

}
