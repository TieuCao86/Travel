package com.example.travel.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TourCardDTO {

    private Integer maTour;
    private String tenTour;
    private String loaiTour;
    private String moTa;
    private String thoiGian;

    private BigDecimal gia;
    private BigDecimal giamGia;

    private Double soSaoTrungBinh;
    private Long soDanhGia;

    private String duongDanAnhDaiDien;

    private List<String> phuongTiens;
    private List<String> lichKhoiHanhs;

    private String noiXuatPhat;
}


