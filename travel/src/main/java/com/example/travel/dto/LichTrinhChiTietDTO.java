package com.example.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LichTrinhChiTietDTO {
    private Integer maChiTiet;
    private Integer thuTu;
    private String loaiNoiDung;
    private String noiDung;
    private String tenThanhPho;
}

