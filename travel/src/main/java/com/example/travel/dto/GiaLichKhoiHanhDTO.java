package com.example.travel.dto;

import lombok.Data;

@Data
public class GiaLoaiKhachDTO {
    private String loaiHanhKhach; // 'NguoiLon', 'TreEm', 'TreNho'
    private Double gia;
}

