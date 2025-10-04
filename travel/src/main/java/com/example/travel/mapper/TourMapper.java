package com.example.travel.mapper;

import com.example.travel.dto.*;
import com.example.travel.model.*;
import com.example.travel.projection.TourCardProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {LichKhoiHanhMapper.class, DanhGiaMapper.class, LichTrinhChiTietMapper.class}
)
public interface TourMapper {

    // Entity -> DetailDTO
    @Mapping(target = "hinhAnhs", source = "hinhAnhTourList", qualifiedByName = "mapHinhAnhs")
    @Mapping(target = "duongDanAnhDaiDien", source = "hinhAnhTourList", qualifiedByName = "mapAnhDaiDien")
    @Mapping(target = "phuongTiens", source = "phuongTiens", qualifiedByName = "mapPhuongTiens")
    @Mapping(target = "lichKhoiHanhs", source = "lichKhoiHanhs")
    @Mapping(target = "danhGiaList", source = "danhGiaList")
    @Mapping(target = "noiXuatPhat", source = ".", qualifiedByName = "mapNoiXuatPhat")
    @Mapping(target = "lichTrinhNgayList", source = ".", qualifiedByName = "mapLichTrinhNgayList")
    @Mapping(target = "soSaoTrungBinh", source = "danhGiaList", qualifiedByName = "tinhSoSaoTrungBinh")
    @Mapping(target = "soDanhGia", expression = "java(tour.getDanhGiaList() != null ? tour.getDanhGiaList().size() : 0)")
    TourDetailDTO toDetailDTO(Tour tour);

    // Entity -> CardDTO
    @Mapping(target = "giamGia", ignore = true)
    @Mapping(target = "phuongTiens", source = "phuongTiens", qualifiedByName = "mapPhuongTiens")
    TourCardDTO toCardDTO(Tour tour);

    // Projection -> CardDTO
    TourCardDTO toCardDTO(TourCardProjection projection);

    // Projection list -> DTO list
    List<TourCardDTO> toCardDTOList(List<TourCardProjection> projections);

    // ===== Helper methods =====

    @Named("mapPhuongTiens")
    default List<String> mapPhuongTiens(List<PhuongTien> phuongTiens) {
        if (phuongTiens == null || phuongTiens.isEmpty()) return List.of();
        return phuongTiens.stream().map(PhuongTien::getTenPhuongTien).toList();
    }

    @Named("mapHinhAnhs")
    default List<String> mapHinhAnhs(List<HinhAnhTour> hinhAnhs) {
        if (hinhAnhs == null || hinhAnhs.isEmpty()) return List.of();
        return hinhAnhs.stream().map(HinhAnhTour::getDuongDan).toList();
    }

    @Named("mapAnhDaiDien")
    default String mapAnhDaiDien(List<HinhAnhTour> hinhAnhs) {
        if (hinhAnhs == null || hinhAnhs.isEmpty()) return null;
        return hinhAnhs.stream()
                .filter(HinhAnhTour::isLaAnhDaiDien)
                .map(HinhAnhTour::getDuongDan)
                .findFirst()
                .orElse(null);
    }

    @Named("tinhSoSaoTrungBinh")
    default Double tinhSoSaoTrungBinh(List<DanhGia> danhGias) {
        if (danhGias == null || danhGias.isEmpty()) return 0.0;
        return danhGias.stream().mapToInt(DanhGia::getSoSao).average().orElse(0.0);
    }

    @Named("mapNoiXuatPhat")
    default String mapNoiXuatPhat(Tour tour) {
        if (tour == null || tour.getLichKhoiHanhs() == null || tour.getLichKhoiHanhs().isEmpty()) return null;
        return tour.getLichKhoiHanhs().stream()
                .filter(lkh -> lkh.getThanhPhoKhoiHanh() != null)
                .sorted((a, b) -> a.getNgayKhoiHanh().compareTo(b.getNgayKhoiHanh()))
                .map(lkh -> lkh.getThanhPhoKhoiHanh().getTenThanhPho())
                .findFirst()
                .orElse(null);
    }

    @Named("mapLichTrinhNgayList")
    default List<LichTrinhNgayDTO> mapLichTrinhNgayList(Tour tour) {
        if (tour == null || tour.getLichTrinhNgayList() == null) return List.of();

        return tour.getLichTrinhNgayList().stream()
                .map(ngay -> {
                    LichTrinhNgayDTO dto = new LichTrinhNgayDTO();
                    dto.setMaNgay(ngay.getMaNgay());
                    dto.setNgayThu(ngay.getNgayThu());
                    dto.setTieuDe(ngay.getTieuDe());
                    dto.setBuaAn(ngay.getBuaAn());
                    dto.setMoTaTongQuan(ngay.getMoTaTongQuan());

                    dto.setAnhNoiBat(
                            ngay.getChiTietList().stream()
                                    .flatMap(ct -> ct.getHinhAnhChiTietList().stream())
                                    .filter(anh -> Boolean.TRUE.equals(anh.getLaAnhNoiBat()))
                                    .map(HinhAnhChiTietTour::getDuongDan)
                                    .findFirst()
                                    .orElse(null)
                    );

                    dto.setHoatDongList(
                            ngay.getChiTietList().stream()
                                    .filter(ct -> "TEXT".equalsIgnoreCase(ct.getLoaiNoiDung()))
                                    .map(LichTrinhChiTiet::getNoiDung)
                                    .toList()
                    );

                    dto.setDsAnhChiTiet(
                            ngay.getChiTietList().stream()
                                    .flatMap(ct -> ct.getHinhAnhChiTietList().stream())
                                    .map(anh -> {
                                        AnhChiTietDTO a = new AnhChiTietDTO();
                                        a.setDuongDan(anh.getDuongDan());
                                        a.setMoTa(anh.getMoTa());
                                        return a;
                                    })
                                    .toList()
                    );

                    return dto;
                })
                .toList();
    }
}
