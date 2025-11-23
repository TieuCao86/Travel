package com.example.travel.mapper;

import com.example.travel.model.HinhAnhTour;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface HinhAnhTourMapper {

    @Named("mapHinhAnhs")
    default List<String> mapHinhAnhs(Set<HinhAnhTour> hinhAnhs) {
        if (hinhAnhs == null || hinhAnhs.isEmpty()) return List.of();
        return hinhAnhs.stream().map(HinhAnhTour::getDuongDan).toList();
    }

    @Named("mapAnhDaiDien")
    default String mapAnhDaiDien(Set<HinhAnhTour> hinhAnhs) {
        if (hinhAnhs == null || hinhAnhs.isEmpty()) return null;
        return hinhAnhs.stream()
                .filter(HinhAnhTour::isLaAnhDaiDien)
                .map(HinhAnhTour::getDuongDan)
                .findFirst()
                .orElse(null);
    }
}
