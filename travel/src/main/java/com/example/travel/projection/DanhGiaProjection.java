package com.example.travel.projection;

import java.time.LocalDateTime;

public interface DanhGiaProjection {
    String getHoTen();       // Tên người đánh giá
    Integer getSoSao();      // Số sao 1-5
    String getBinhLuan();    // Bình luận
    LocalDateTime getNgayDanhGia(); // Ngày đánh giá
}
