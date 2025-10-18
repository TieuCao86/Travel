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
    private String moTa; // mô tả ngắn gọn
    private String thoiGian;
    private BigDecimal gia;
    private List<String> phuongTiens;
    private String duongDanAnhDaiDien;
    private Double soSaoTrungBinh;
    private Integer soDanhGia;
    private String giamGia;
}
