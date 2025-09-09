package com.example.travel.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TourDetailDTO {
    private Integer maTour;
    private String tenTour;
    private String loaiTour;
    private String moTa;
    private String thoiGian;
    private List<GiaLoaiKhachDTO> giaList;

    private Double giamGia;
    private Double soSaoTrungBinh;
    private Integer soDanhGia;
    private String duongDanAnhDaiDien;
    private List<String> hinhAnhs;
    private List<String> phuongTiens;
    private List<LichKhoiHanhDTO> lichKhoiHanhs;
    private List<DanhGiaDTO> danhGiaList;
}


