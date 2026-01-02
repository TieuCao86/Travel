package com.example.travel.projection;

import java.time.LocalDate;
import java.util.List;

public interface LichKhoiHanhProjection {

    Integer getMaLichKhoiHanh();
    LocalDate getNgayKhoiHanh();
    LocalDate getNgayKetThuc();
    LocalDate getHanChotDangKy();
    Integer getSoCho();
    String getTrangThai();

    String getThanhPhoKhoiHanh(); // nếu query alias
    List<GiaLichKhoiHanhProjection> getGiaLichKhoiHanhs();
}
