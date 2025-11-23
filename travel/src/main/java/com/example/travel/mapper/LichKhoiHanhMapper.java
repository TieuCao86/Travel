package com.example.travel.mapper;

import com.example.travel.dto.GiaLoaiKhachDTO;
import com.example.travel.dto.LichKhoiHanhDTO;
import com.example.travel.model.GiaLichKhoiHanh;
import com.example.travel.model.LichKhoiHanh;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LichKhoiHanhMapper {

    @Mapping(target = "tenThanhPhoKhoiHanh", source = "thanhPhoKhoiHanh.tenThanhPho")
    @Mapping(target = "giaList", expression = "java(mapGiaLichKhoiHanh(lichKhoiHanh.getGiaLichKhoiHanhs()))")
    LichKhoiHanhDTO toDTO(LichKhoiHanh lichKhoiHanh);

    List<LichKhoiHanhDTO> toDTOList(List<LichKhoiHanh> lichKhoiHanhs);

    default List<GiaLoaiKhachDTO> mapGiaLichKhoiHanh(List<GiaLichKhoiHanh> giaList) {
        if (giaList == null) return List.of();
        return giaList.stream()
                .map(g -> {
                    GiaLoaiKhachDTO dto = new GiaLoaiKhachDTO();
                    dto.setLoaiHanhKhach(g.getLoaiHanhKhach());
                    dto.setGia(g.getGia());
                    return dto;
                })
                .toList();
    }

    @Named("mapLichCard")
    default List<String> mapLichCard(String lichKhoiHanhs) {
        if (lichKhoiHanhs == null || lichKhoiHanhs.isEmpty()) return Collections.emptyList();
        return List.of(lichKhoiHanhs.split(",")); // hoặc ", " nếu có space
    }

}

