package com.example.travel.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GiaLichKhoiHanhDTO {
    private Integer maGia;
    private String loaiKhach;
    private BigDecimal gia;
}

