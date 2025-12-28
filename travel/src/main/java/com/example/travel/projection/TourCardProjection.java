package com.example.travel.projection;

import java.math.BigDecimal;

public interface TourCardProjection {
    Integer getMaTour();
    String getTenTour();
    String getLoaiTour();
    String getMoTa();
    String getThoiGian();

    BigDecimal getGia();
    BigDecimal getGiamGia();

    String getDuongDanAnhDaiDien();

    Double getSoSaoTrungBinh();
    Long getSoDanhGia();

    String getPhuongTiens();
    String getNoiXuatPhat();
    String getLichKhoiHanhs();
}




