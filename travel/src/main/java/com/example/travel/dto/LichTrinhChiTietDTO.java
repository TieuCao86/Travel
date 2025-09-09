package com.example.travel.dto;

import lombok.Data;

@Data
public class LichTrinhChiTietDTO {
    private Integer maChiTiet;
    private Integer ngayThu;       // Ngày thứ mấy
    private String buoi;           // Sáng / Chiều / Tối
    private String hoatDong;       // Nội dung hoạt động
    private String tenThanhPho;    // Thành phố
}
