package com.example.travel.dto;

import lombok.Data;
import java.util.List;

@Data
public class LichTrinhNgayDTO {
    private Integer maNgay;
    private Integer ngayThu;
    private String tieuDe;
    private String buaAn;
    private String moTaTongQuan;

    // ảnh nổi bật của ngày
    private String anhNoiBat;

    // danh sách hoạt động trong ngày
    private List<String> hoatDongList;

    // danh sách ảnh chi tiết (có cả mô tả)
    private List<AnhChiTietDTO> dsAnhChiTiet;
}
