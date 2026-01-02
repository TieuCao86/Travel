package com.example.travel.mapper;

import com.example.travel.dto.LichKhoiHanhDTO;
import com.example.travel.model.LichKhoiHanh;
import com.example.travel.model.GiaLichKhoiHanh;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = GiaLichKhoiHanhMapper.class
)
public interface LichKhoiHanhMapper {

    @Mapping(source = "giaLichKhoiHanhs", target = "giaList")
    LichKhoiHanhDTO toDTO(LichKhoiHanh entity);

    List<LichKhoiHanhDTO> toDTOList(List<LichKhoiHanh> entities);
}


