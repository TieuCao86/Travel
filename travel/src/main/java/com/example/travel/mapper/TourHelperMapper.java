package com.example.travel.mapper;

import com.example.travel.dto.*;
import com.example.travel.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface TourHelperMapper {

    @Named("mapPhuongTiens")
    default List<String> mapPhuongTiens(List<PhuongTien> phuongTiens) {
        if (phuongTiens == null || phuongTiens.isEmpty()) return List.of();
        return phuongTiens.stream().map(PhuongTien::getTenPhuongTien).toList();
    }

    @Named("tinhSoSaoTrungBinh")
    default Double tinhSoSaoTrungBinh(Set<DanhGia> danhGias) {
        if (danhGias == null || danhGias.isEmpty()) return 0.0;
        return danhGias.stream().mapToInt(DanhGia::getSoSao).average().orElse(0.0);
    }

    @Named("mapNoiXuatPhat")
    default String mapNoiXuatPhat(Tour tour) {
        if (tour == null) return null;

        return tour.getLichKhoiHanhs()
                .stream()
                .filter(lkh -> lkh.getThanhPhoKhoiHanh() != null)
                .sorted(Comparator.comparing(
                        LichKhoiHanh::getNgayKhoiHanh,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ))
                .map(lkh -> lkh.getThanhPhoKhoiHanh().getTenThanhPho())
                .findFirst()
                .orElse(null);
    }
}
