package com.example.travel.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class LichKhoiHanhDTO {

    private Integer maLichKhoiHanh;
    private LocalDate ngayKhoiHanh;
    private LocalDate ngayKetThuc;
    private LocalDate hanChotDangKy;
    private Integer soCho;
    private String trangThai;

    private List<GiaLichKhoiHanhDTO> giaList;
}

