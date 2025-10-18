package com.example.travel.mapper;

import com.example.travel.dto.AnhChiTietDTO;
import com.example.travel.dto.LichTrinhChiTietDTO;
import com.example.travel.model.HinhAnhChiTietTour;
import com.example.travel.model.LichTrinhChiTiet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LichTrinhChiTietMapper {

    // Gọi hàm mapAnhChiTiet được đánh dấu @Named("mapAnhChiTiet")
    @Mapping(target = "dsAnhChiTiet", source = "hinhAnhChiTietTours", qualifiedByName = "mapAnhChiTiet")
    LichTrinhChiTietDTO toDTO(LichTrinhChiTiet entity);

    @Named("mapAnhChiTiet")
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
