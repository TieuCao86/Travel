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
    private List<LichTrinhChiTietDTO> hoatDongList = List.of();
    private String anhNoiBat;
}

