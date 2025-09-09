package com.example.travel.mapper;

import com.example.travel.dto.DanhGiaDTO;
import com.example.travel.model.DanhGia;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DanhGiaMapper {
    DanhGiaDTO toDTO(DanhGia danhGia);
}
