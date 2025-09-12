package com.example.travel.mapper;

import com.example.travel.dto.LichTrinhChiTietDTO;
import com.example.travel.model.LichTrinhChiTiet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LichTrinhChiTietMapper {

    @Mapping(target = "tenThanhPho", source = "thanhPho.tenThanhPho")
    LichTrinhChiTietDTO toDTO(LichTrinhChiTiet lichTrinhChiTiet);

    List<LichTrinhChiTietDTO> toDTOList(List<LichTrinhChiTiet> entities);
}

