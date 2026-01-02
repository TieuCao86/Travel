package com.example.travel.mapper;

import com.example.travel.dto.*;
import com.example.travel.model.DanhGia;
import com.example.travel.model.GiaLichKhoiHanh;
import com.example.travel.model.LichKhoiHanh;
import com.example.travel.projection.TourCardProjection;
import com.example.travel.projection.TourDetailProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = { LichKhoiHanhMapper.class }
)
public interface TourMapper {

    /* ================= HOME ================= */

    @Mapping(target = "phuongTiens", source = "phuongTiens", qualifiedByName = "split")
    @Mapping(target = "lichKhoiHanhs", source = "lichKhoiHanhs", qualifiedByName = "split")
    @Mapping(target = "noiXuatPhat", source = "noiXuatPhat")
    TourCardDTO toCardDTO(TourCardProjection p);

    List<TourCardDTO> toCardDTOList(List<TourCardProjection> projections);

    /* ================= DETAIL ================= */

    @Mapping(target = "phuongTiens", source = "phuongTiens", qualifiedByName = "split")
    @Mapping(target = "hinhAnhs", source = "hinhAnhs", qualifiedByName = "split")
    @Mapping(target = "thanhPhos", source = "thanhPhos", qualifiedByName = "mapThanhPhoFromString")
    @Mapping(target = "lichTrinhNgayList", source = "lichTrinhNgays", qualifiedByName = "mapLichTrinhNgay")
    TourDetailDTO toDetailDTO(TourDetailProjection p);

    /* ================= HELPER ================= */

    @Named("split")
    default List<String> split(String value) {
        return value == null || value.isBlank()
                ? List.of()
                : Arrays.stream(value.split("\\s*,\\s*")).toList();
    }

    @Named("mapThanhPhoFromString")
    default List<ThanhPhoDTO> mapThanhPhoFromString(String value) {
        if (value == null || value.isBlank()) return List.of();

        return Arrays.stream(value.split("\\s*,\\s*"))
                .map(name -> {
                    ThanhPhoDTO dto = new ThanhPhoDTO();
                    dto.setTenThanhPho(name);
                    return dto;
                })
                .toList();
    }

    @Named("mapLichTrinhNgay")
    default List<LichTrinhNgayDTO> mapLichTrinhNgay(String value) {
        if (value == null || value.isBlank()) return List.of();

        return Arrays.stream(value.split(";;"))
                .map(row -> {
                    String[] parts = row.split("\\|");
                    LichTrinhNgayDTO dto = new LichTrinhNgayDTO();
                    dto.setNgayThu(Integer.parseInt(parts[0]));
                    dto.setTieuDe(parts[1]);
                    dto.setBuaAn(parts[2]);
                    dto.setMoTaTongQuan(parts[3]);
                    return dto;
                })
                .toList();
    }
}
