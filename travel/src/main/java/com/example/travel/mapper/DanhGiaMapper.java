package com.example.travel.mapper;

import com.example.travel.dto.DanhGiaDTO;
import com.example.travel.model.DanhGia;
import com.example.travel.projection.DanhGiaProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DanhGiaMapper {

    DanhGiaDTO toDTO(DanhGiaProjection projection);

    List<DanhGiaDTO> toDTOList(List<DanhGiaProjection> list);
}

