package com.example.travel.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DanhGiaDTO {
    private Integer maDanhGia;
    private String tenNguoiDung;
    private int soSao;
    private String binhLuan;
    private LocalDateTime ngayDanhGia;
}
