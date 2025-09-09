package com.example.travel.projection;

import java.math.BigDecimal;

public interface TourCardProjection {
    int getMaTour();
    String getTenTour();
    String getLoaiTour();
    String getMoTa();
    String getThoiGian();
    BigDecimal getGia();
    String getDuongDanAnhDaiDien();
    Double getSoSaoTrungBinh();
    Integer getSoDanhGia();
    String getPhuongTien();   // ✅ khớp alias SQL
    BigDecimal getGiamGia();  // ✅ kiểu dữ liệu đúng với SQL
}
