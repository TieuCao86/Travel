package com.example.travel.mapper;

import com.example.travel.dto.GiaLoaiKhachDTO;
import com.example.travel.dto.TourCardDTO;
import com.example.travel.dto.TourDetailDTO;
import com.example.travel.model.*;
import com.example.travel.projection.TourCardProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {LichKhoiHanhMapper.class, DanhGiaMapper.class, LichTrinhChiTietMapper.class}
)
public interface TourMapper {

    // Entity -> DetailDTO
    @Mapping(target = "hinhAnhs", expression = "java(mapHinhAnhs(tour.getHinhAnhTours()))")
    @Mapping(target = "duongDanAnhDaiDien", expression = "java(mapAnhDaiDien(tour.getHinhAnhTours()))")
    @Mapping(target = "phuongTiens", expression = "java(mapPhuongTiens(tour.getPhuongTiens()))")
    @Mapping(target = "lichKhoiHanhs", source = "lichKhoiHanhs")
    @Mapping(target = "danhGiaList", source = "danhGiaList")
    @Mapping(target = "lichTrinhChiTietList", source = "lichTrinhList")
    @Mapping(target = "soSaoTrungBinh", expression = "java(tinhSoSaoTrungBinh(tour.getDanhGiaList()))")
    @Mapping(target = "soDanhGia", expression = "java(tour.getDanhGiaList() != null ? tour.getDanhGiaList().size() : 0)")
    TourDetailDTO toDetailDTO(Tour tour);

    // Entity -> CardDTO
    @Mapping(target = "giamGia", ignore = true)
    @Mapping(target = "phuongTiens", expression = "java(mapPhuongTiens(tour.getPhuongTiens()))")
    TourCardDTO toCardDTO(Tour tour);

    // Projection -> CardDTO
    TourCardDTO toCardDTO(TourCardProjection projection);

    // Projection list -> DTO list
    List<TourCardDTO> toCardDTOList(List<TourCardProjection> projections);

    // ===== Helpers =====
    default List<String> mapPhuongTiens(List<PhuongTien> phuongTiens) {
        if (phuongTiens == null || phuongTiens.isEmpty()) return List.of();
        return phuongTiens.stream()
                .map(PhuongTien::getTenPhuongTien)
                .toList();
    }

    default List<String> mapHinhAnhs(List<HinhAnhTour> hinhAnhs) {
        if (hinhAnhs == null || hinhAnhs.isEmpty()) return List.of();
        return hinhAnhs.stream()
                .map(HinhAnhTour::getDuongDan)
                .toList();
    }

    default String mapAnhDaiDien(List<HinhAnhTour> hinhAnhs) {
        if (hinhAnhs == null || hinhAnhs.isEmpty()) return null;
        return hinhAnhs.stream()
                .filter(HinhAnhTour::isLaAnhDaiDien)
                .map(HinhAnhTour::getDuongDan)
                .findFirst()
                .orElse(null);
    }

    default Double tinhSoSaoTrungBinh(List<DanhGia> danhGias) {
        if (danhGias == null || danhGias.isEmpty()) return 0.0;
        return danhGias.stream()
                .mapToInt(DanhGia::getSoSao)
                .average()
                .orElse(0.0);
    }

    default List<GiaLoaiKhachDTO> getGiaHienTai(Tour tour) {
        if (tour == null || tour.getLichKhoiHanhs() == null) return List.of();

        return tour.getLichKhoiHanhs().stream()
                .flatMap(lkh -> {
                    if (lkh.getGiaLichKhoiHanhs() == null) return List.<GiaLoaiKhachDTO>of().stream();
                    return lkh.getGiaLichKhoiHanhs().stream()
                            .map(g -> {
                                GiaLoaiKhachDTO dto = new GiaLoaiKhachDTO();
                                dto.setLoaiHanhKhach(g.getLoaiHanhKhach());
                                dto.setGia(g.getGia().doubleValue());
                                return dto;
                            });
                })
                .toList();
    }



}
