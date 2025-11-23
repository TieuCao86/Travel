package com.example.travel.mapper;

import com.example.travel.dto.*;
import com.example.travel.model.*;
import com.example.travel.projection.TourCardProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                PhuongTienMapper.class,
                LichKhoiHanhMapper.class,
                DanhGiaMapper.class,
                LichTrinhChiTietMapper.class,
                HinhAnhTourMapper.class,
                ThanhPhoMapper.class,
                VoucherMapper.class,
                TourHelperMapper.class
        }
)
public interface TourMapper {

    @Mapping(target = "hinhAnhs", source = "hinhAnhTourList", qualifiedByName = "mapHinhAnhs")
    @Mapping(target = "duongDanAnhDaiDien", source = "hinhAnhTourList", qualifiedByName = "mapAnhDaiDien")
    @Mapping(target = "phuongTiens", source = "phuongTiens", qualifiedByName = "mapPhuongTiens")
    @Mapping(target = "lichKhoiHanhs", source = "lichKhoiHanhs") // map List<LichKhoiHanh> -> List<LichKhoiHanhDTO>
    @Mapping(target = "danhGiaList", source = "danhGiaList")
    @Mapping(target = "noiXuatPhat", source = ".", qualifiedByName = "mapNoiXuatPhat")
    @Mapping(target = "soSaoTrungBinh", source = "danhGiaList", qualifiedByName = "tinhSoSaoTrungBinh")
    @Mapping(target = "soDanhGia", expression = "java(tour.getDanhGiaList() != null ? tour.getDanhGiaList().size() : 0)")
    @Mapping(target = "giamGia", source = "vouchers", qualifiedByName = "mapGiamGia")
    @Mapping(target = "thanhPhos", source = "thanhPhos", qualifiedByName = "mapThanhPhos")
    TourDetailDTO toDetailDTO(Tour tour);

    @Mapping(target = "lichKhoiHanhs", source = "lichKhoiHanhs", qualifiedByName = "mapLichCard")
    @Mapping(target = "phuongTiens", source = "phuongTiens", qualifiedByName = "splitStringToList")
    TourCardDTO toCardDTO(TourCardProjection projection);

    List<TourCardDTO> toCardDTOList(List<TourCardProjection> projections);
}


