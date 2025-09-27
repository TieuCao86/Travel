package com.example.travel.dto;

import lombok.Data;

import java.util.List;

@Data
public class LichTrinhNgayDTO {
    private Integer soNgay;   // hoặc ngayThu
    private String moTaNgay;
    private List<LichTrinhChiTietDTO> chiTietList;
}

