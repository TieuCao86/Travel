package com.example.travel.mapper;

import com.example.travel.dto.AnhChiTietDTO;
import com.example.travel.dto.LichTrinhChiTietDTO;
import com.example.travel.model.HinhAnhChiTietTour;
import com.example.travel.model.LichTrinhChiTiet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LichTrinhChiTietMapper {

    @Mapping(target = "dsAnhChiTiet", expression = "java(mapAnhChiTiet(entity.getHinhAnhChiTietTours()))")
    LichTrinhChiTietDTO toDTO(LichTrinhChiTiet entity);

    default List<AnhChiTietDTO> mapAnhChiTiet(List<HinhAnhChiTietTour> hinhAnhs) {
        if (hinhAnhs == null || hinhAnhs.isEmpty()) return List.of();
        return hinhAnhs.stream()
                .map(anh -> {
                    AnhChiTietDTO dto = new AnhChiTietDTO();
                    dto.setDuongDan(anh.getDuongDan());
                    dto.setMoTa(anh.getMoTa());
                    return dto;
                })
                .toList();
    }
}
