package com.example.travel.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TourDetailDTO {

    private Integer maTour;
    private String tenTour;
    private String loaiTour;
    private String moTa;
    private String thoiGian;

    private String quocGia;
    private String duongDanAnhDaiDien;

    /* ===== GIÁ ===== */
    private BigDecimal giaTu;
    private BigDecimal giamGia;

    private Double soSaoTrungBinh;
    private Integer soDanhGia;

    /* ===== LIST ===== */
    private List<String> hinhAnhs;
    private List<String> phuongTiens;
    private List<LichKhoiHanhDTO> lichKhoiHanhs;
    private List<ThanhPhoDTO> thanhPhos;
    private List<LichTrinhNgayDTO> lichTrinhNgayList;
    private List<DanhGiaDTO> danhGiaList;
}


