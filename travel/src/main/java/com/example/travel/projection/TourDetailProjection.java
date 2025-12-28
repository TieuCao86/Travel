package com.example.travel.projection;

import java.math.BigDecimal;

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

    /* ====== STRING_AGG ====== */
    String getThanhPhos();        // "Hà Nội, Đà Nẵng"
    String getPhuongTiens();      // "Máy bay, Xe du lịch"
    String getHinhAnhs();         // "img1.jpg,img2.jpg"
    String getLichKhoiHanhs();    // "2025-01-01,2025-01-15"
    String getLichTrinhNgays();
}
