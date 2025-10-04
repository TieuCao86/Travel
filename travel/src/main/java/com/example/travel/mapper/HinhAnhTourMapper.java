package com.example.travel.mapper;

import com.example.travel.dto.HinhAnhTourDTO;
import com.example.travel.model.HinhAnhTour;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HinhAnhTourMapper {
    HinhAnhTourMapper INSTANCE = Mappers.getMapper(HinhAnhTourMapper.class);

    HinhAnhTourDTO toDTO(HinhAnhTour entity);
    List<HinhAnhTourDTO> toDTOs(List<HinhAnhTour> entities);
}