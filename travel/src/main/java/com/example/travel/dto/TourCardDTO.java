package com.example.travel.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class TourCardDTO {
    private int maTour;
    private String tenTour;
    private String loaiTour;
    private String moTa;
    private String thoiGian;
    private String noiXuatPhat;
    private BigDecimal giamGia;
    private Double soSaoTrungBinh;
    private Long soDanhGia;
    private String duongDanAnhDaiDien;
    private List<String> phuongTiens;
    private List<String> lichKhoiHanhs;
    private BigDecimal gia;

}

