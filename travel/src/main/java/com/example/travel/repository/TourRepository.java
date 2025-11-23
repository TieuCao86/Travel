package com.example.travel.repository;

import com.example.travel.model.Tour;
import com.example.travel.projection.TourCardProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TourRepository extends JpaRepository<Tour, Integer>, JpaSpecificationExecutor<Tour> {

    @Query(value = """
    SELECT 
        T.MaTour AS maTour,
        T.TenTour AS tenTour,
        T.LoaiTour AS loaiTour,
        T.MoTa AS moTa,
        T.ThoiGian AS thoiGian,

        -- Giá người lớn của lịch gần nhất
        (SELECT TOP 1 GLKH.Gia
         FROM LichKhoiHanh LK2
         JOIN GiaLichKhoiHanh GLKH ON LK2.MaLichKhoiHanh = GLKH.MaLichKhoiHanh
         WHERE LK2.MaTour = T.MaTour AND GLKH.LoaiHanhKhach = 'NguoiLon'
         ORDER BY LK2.NgayKhoiHanh ASC
        ) AS gia,

        DG.SoSaoTrungBinh AS soSaoTrungBinh,
        DG.SoDanhGia AS soDanhGia,
        A.DuongDan AS duongDanAnhDaiDien,
        PT.PhuongTien AS phuongTiens,
        V.GiamGia AS giamGia,

        -- Tất cả ngày khởi hành
        (SELECT STRING_AGG(CONVERT(varchar, LK.NgayKhoiHanh, 23), ',')
         FROM LichKhoiHanh LK
         WHERE LK.MaTour = T.MaTour
        ) AS lichKhoiHanhs

    FROM Tour T

    -- Rating
    LEFT JOIN (
        SELECT MaTour,
               AVG(CAST(SoSao AS FLOAT)) AS SoSaoTrungBinh,
               COUNT(*) AS SoDanhGia
        FROM DanhGia
        GROUP BY MaTour
    ) DG ON DG.MaTour = T.MaTour

    -- Phương tiện
    LEFT JOIN (
        SELECT TP.MaTour,
               STRING_AGG(PT.TenPhuongTien, ', ') AS PhuongTien
        FROM TourPhuongTien TP
        JOIN PhuongTien PT ON TP.MaPhuongTien = PT.MaPhuongTien
        GROUP BY TP.MaTour
    ) PT ON PT.MaTour = T.MaTour

    -- Voucher
    LEFT JOIN (
        SELECT TV.MaTour,
               MAX(V.GiaTri) AS GiamGia
        FROM TourVoucher TV
        JOIN Voucher V ON TV.MaVoucher = V.MaVoucher
        WHERE V.NgayHetHan >= GETDATE()
        GROUP BY TV.MaTour
    ) V ON V.MaTour = T.MaTour

    -- Ảnh đại diện
    LEFT JOIN HinhAnhTour A 
           ON A.MaTour = T.MaTour AND A.LaAnhDaiDien = 1

    -- Filter
    WHERE 
        (:tenTour IS NULL OR T.TenTour LIKE CONCAT('%', :tenTour, '%'))
        AND (:loaiTour IS NULL OR T.LoaiTour = :loaiTour)
        AND (:thanhPho IS NULL OR EXISTS (
                SELECT 1
                FROM ThanhPho_Tour TP
                JOIN ThanhPho P ON TP.MaThanhPho = P.MaThanhPho
                JOIN QuocGia Q ON P.MaQuocGia = Q.MaQuocGia
                WHERE TP.MaTour = T.MaTour
                  AND (P.TenThanhPho LIKE CONCAT('%', :thanhPho, '%')
                       OR Q.TenQuocGia LIKE CONCAT('%', :thanhPho, '%'))
        ))
        AND (:minGia IS NULL OR 
             (SELECT TOP 1 GLKH.Gia
              FROM LichKhoiHanh LK2
              JOIN GiaLichKhoiHanh GLKH ON LK2.MaLichKhoiHanh = GLKH.MaLichKhoiHanh
              WHERE LK2.MaTour = T.MaTour AND GLKH.LoaiHanhKhach = 'NguoiLon'
              ORDER BY LK2.NgayKhoiHanh ASC) >= :minGia)
        AND (:maxGia IS NULL OR 
             (SELECT TOP 1 GLKH.Gia
              FROM LichKhoiHanh LK2
              JOIN GiaLichKhoiHanh GLKH ON LK2.MaLichKhoiHanh = GLKH.MaLichKhoiHanh
              WHERE LK2.MaTour = T.MaTour AND GLKH.LoaiHanhKhach = 'NguoiLon'
              ORDER BY LK2.NgayKhoiHanh ASC) <= :maxGia)

    ORDER BY DG.SoSaoTrungBinh DESC
    OFFSET :offset ROWS
    FETCH NEXT :limit ROWS ONLY
""", nativeQuery = true)
    List<TourCardProjection> getFullTourCards(
            @Param("tenTour") String tenTour,
            @Param("loaiTour") String loaiTour,
            @Param("thanhPho") String thanhPho,
            @Param("minGia") BigDecimal minGia,
            @Param("maxGia") BigDecimal maxGia,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    // Nếu muốn fetch đầy đủ quan hệ tránh n+1:
    @Query("SELECT t FROM Tour t " +
            "LEFT JOIN FETCH t.lichKhoiHanhs lk " +
            "LEFT JOIN FETCH t.danhGiaList " +
            "LEFT JOIN FETCH t.hinhAnhTourList " +
            "LEFT JOIN FETCH t.thanhPhos " +
            "LEFT JOIN FETCH t.vouchers " +
            "WHERE t.maTour = :id")
    Optional<Tour> findFullDetailById(@Param("id") Integer id);


}
