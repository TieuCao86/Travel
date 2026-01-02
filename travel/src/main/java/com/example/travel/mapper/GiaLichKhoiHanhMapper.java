package com.example.travel.mapper;

import com.example.travel.dto.GiaLichKhoiHanhDTO;
import com.example.travel.model.GiaLichKhoiHanh;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GiaLichKhoiHanhMapper {

    GiaLichKhoiHanhDTO toDTO(GiaLichKhoiHanh entity);

    List<GiaLichKhoiHanhDTO> toDTOList(List<GiaLichKhoiHanh> entities);
}

