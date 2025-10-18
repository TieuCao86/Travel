package com.example.travel.dto;

import lombok.Data;

@Data
public class ThanhPhoDTO {
    private Integer maThanhPho;
    private String tenThanhPho;
    private String moTa;
    private String duongDanAnh;
    private String tenQuocGia; // lấy từ bảng QuocGia
}
