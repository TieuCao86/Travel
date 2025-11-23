package com.example.travel.mapper;

import com.example.travel.dto.ThanhPhoDTO;
import com.example.travel.model.ThanhPho;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ThanhPhoMapper {

    @Named("mapThanhPhos")
    default List<ThanhPhoDTO> mapThanhPhos(Set<ThanhPho> thanhPhos) {
        if (thanhPhos == null || thanhPhos.isEmpty()) return List.of();

        return thanhPhos.stream().map(tp -> {
            ThanhPhoDTO dto = new ThanhPhoDTO();
            dto.setMaThanhPho(tp.getMaThanhPho());
            dto.setTenThanhPho(tp.getTenThanhPho());
            dto.setMoTa(tp.getMoTa());
            dto.setDuongDanAnh(tp.getDuongDanAnh());
            if (tp.getQuocGia() != null) {
                dto.setTenQuocGia(tp.getQuocGia().getTenQuocGia());
            }
            return dto;
        }).toList();
    }
}
