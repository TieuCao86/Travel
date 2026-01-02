package com.example.travel.projection;

import com.example.travel.model.LichKhoiHanh;

import java.math.BigDecimal;
import java.util.List;

public interface TourDetailProjection {

    Integer getMaTour();
    String getTenTour();
    String getLoaiTour();
    String getMoTa();
    String getThoiGian();
    String getQuocGia();
    String getDuongDanAnhDaiDien();
    Double getSoSaoTrungBinh();
    Integer getSoDanhGia();
    BigDecimal getGiamGia();

    String getThanhPhos();
    String getPhuongTiens();
    String getHinhAnhs();
    String getLichTrinhNgays();
}

