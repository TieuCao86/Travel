package com.example.travel.repository;

import com.example.travel.model.Tour;
import com.example.travel.projection.TourCardProjection;
import com.example.travel.projection.TourDetailProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TourRepository
        extends JpaRepository<Tour, Integer>, JpaSpecificationExecutor<Tour> {

    /* =====================================================
     * HOME PAGE – 1 QUERY
     * ===================================================== */
    @Query(value = """
SELECT
    T.MaTour AS maTour,
    T.TenTour AS tenTour,
    LT.TenLoai AS loaiTour,
    T.MoTa AS moTa,
    T.ThoiGian AS thoiGian,

    GiaNL.Gia AS gia,

    DG.SoSaoTrungBinh AS soSaoTrungBinh,
    DG.SoDanhGia AS soDanhGia,
    A.DuongDan AS duongDanAnhDaiDien,
    PT.PhuongTien AS phuongTiens,
    V.GiamGia AS giamGia,

    (
        SELECT STRING_AGG(CONVERT(varchar, LK.NgayKhoiHanh, 23), ',')
        FROM LichKhoiHanh LK
        WHERE LK.MaTour = T.MaTour
    ) AS lichKhoiHanhs

FROM Tour T

-- ===== LOẠI TOUR =====
LEFT JOIN Tour_LoaiTour TLT ON T.MaTour = TLT.MaTour
LEFT JOIN LoaiTour LT ON TLT.MaLoaiTour = LT.MaLoaiTour

-- ===== GIÁ NGƯỜI LỚN =====
OUTER APPLY (
    SELECT TOP 1 GLKH.Gia
    FROM LichKhoiHanh LK2
    JOIN GiaLichKhoiHanh GLKH
        ON LK2.MaLichKhoiHanh = GLKH.MaLichKhoiHanh
    WHERE LK2.MaTour = T.MaTour
      AND GLKH.LoaiHanhKhach = 'NguoiLon'
    ORDER BY LK2.NgayKhoiHanh ASC
) GiaNL

-- ===== ĐÁNH GIÁ =====
LEFT JOIN (
    SELECT MaTour,
           AVG(CAST(SoSao AS FLOAT)) AS SoSaoTrungBinh,
           COUNT(*) AS SoDanhGia
    FROM DanhGia
    GROUP BY MaTour
) DG ON DG.MaTour = T.MaTour

-- ===== PHƯƠNG TIỆN =====
LEFT JOIN (
    SELECT TP.MaTour,
           STRING_AGG(PT.TenPhuongTien, ', ') AS PhuongTien
    FROM Tour_PhuongTien TP
    JOIN PhuongTien PT ON TP.MaPhuongTien = PT.MaPhuongTien
    GROUP BY TP.MaTour
) PT ON PT.MaTour = T.MaTour

-- ===== VOUCHER =====
LEFT JOIN (
    SELECT TV.MaTour,
           MAX(V.GiaTri) AS GiamGia
    FROM Tour_Voucher TV
    JOIN Voucher V ON TV.MaVoucher = V.MaVoucher
    WHERE V.NgayHetHan >= GETDATE()
    GROUP BY TV.MaTour
) V ON V.MaTour = T.MaTour

-- ===== ẢNH ĐẠI DIỆN =====
LEFT JOIN HinhAnhTour A
    ON A.MaTour = T.MaTour
   AND A.LaAnhDaiDien = 1

WHERE
    (:tenTour IS NULL OR T.TenTour LIKE CONCAT('%', :tenTour, '%'))
    AND (:loaiTour IS NULL OR LT.TenLoai = :loaiTour)
    AND (:minGia IS NULL OR GiaNL.Gia >= :minGia)
    AND (:maxGia IS NULL OR GiaNL.Gia <= :maxGia)

ORDER BY
    CASE WHEN :sort = 'rating' THEN DG.SoSaoTrungBinh END DESC,
    CASE WHEN :sort = 'price'  THEN GiaNL.Gia END ASC,
    CASE WHEN :sort = 'sale'   THEN V.GiamGia END DESC,
    CASE WHEN :sort = 'new'    THEN T.MaTour END DESC

OFFSET :offset ROWS
FETCH NEXT :limit ROWS ONLY
""", nativeQuery = true)
    List<TourCardProjection> getHomeTours(
            @Param("tenTour") String tenTour,
            @Param("loaiTour") String loaiTour,
            @Param("minGia") BigDecimal minGia,
            @Param("maxGia") BigDecimal maxGia,
            @Param("sort") String sort,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    /* =====================================================
     * TOUR DETAIL – 1 QUERY
     * ===================================================== */
    @Query(value = """
            SELECT
                            t.MaTour AS maTour,
                            t.TenTour AS tenTour,
            
                            -- LOẠI TOUR
                            (
                                SELECT STRING_AGG(x.TenLoai, ', ')
                                FROM (
                                    SELECT DISTINCT lt2.TenLoai
                                    FROM Tour_LoaiTour tlt2
                                    JOIN LoaiTour lt2 ON tlt2.MaLoaiTour = lt2.MaLoaiTour
                                    WHERE tlt2.MaTour = t.MaTour
                                ) x
                            ) AS loaiTour,
            
                            t.MoTa AS moTa,
                            t.ThoiGian AS thoiGian,
            
                            qg.TenQuocGia AS quocGia,
                            a.DuongDan AS duongDanAnhDaiDien,
            
                            dg.SoSaoTrungBinh AS soSaoTrungBinh,
                            dg.SoDanhGia AS soDanhGia,
            
                            v.GiamGia AS giamGia,
            
                            -- THÀNH PHỐ
                            (
                                SELECT STRING_AGG(x.TenThanhPho, ', ')
                                FROM (
                                    SELECT DISTINCT tp2.TenThanhPho
                                    FROM Tour_ThanhPho ttp2
                                    JOIN ThanhPho tp2 ON ttp2.MaThanhPho = tp2.MaThanhPho
                                    WHERE ttp2.MaTour = t.MaTour
                                ) x
                            ) AS thanhPhos,
            
                            -- PHƯƠNG TIỆN
                            (
                                SELECT STRING_AGG(x.TenPhuongTien, ', ')
                                FROM (
                                    SELECT DISTINCT pt2.TenPhuongTien
                                    FROM Tour_PhuongTien tpt2
                                    JOIN PhuongTien pt2 ON tpt2.MaPhuongTien = pt2.MaPhuongTien
                                    WHERE tpt2.MaTour = t.MaTour
                                ) x
                            ) AS phuongTiens,
            
                            -- HÌNH ẢNH
                            (
                                SELECT STRING_AGG(x.DuongDan, ',')
                                FROM (
                                    SELECT DISTINCT ha2.DuongDan
                                    FROM HinhAnhTour ha2
                                    WHERE ha2.MaTour = t.MaTour
                                ) x
                            ) AS hinhAnhs,
            
                            -- LỊCH KHỞI HÀNH
                            (
                                SELECT STRING_AGG(CONVERT(varchar, lk.NgayKhoiHanh, 23), ',')
                                FROM LichKhoiHanh lk
                                WHERE lk.MaTour = t.MaTour
                            ) AS lichKhoiHanhs,
            
                            -- LỊCH TRÌNH NGÀY
                            (
                                SELECT STRING_AGG(
                                    CONCAT(
                                        lt.NgayThu, '|',
                                        lt.TieuDe, '|',
                                        lt.BuaAn, '|',
                                        lt.MoTaTongQuan
                                    ),
                                    ';;'
                                )
                                FROM LichTrinhNgay lt
                                WHERE lt.MaTour = t.MaTour
                            ) AS lichTrinhNgays
            
                        FROM Tour t
                        LEFT JOIN Tour_ThanhPho ttp ON t.MaTour = ttp.MaTour
                        LEFT JOIN ThanhPho tp ON ttp.MaThanhPho = tp.MaThanhPho
                        LEFT JOIN QuocGia qg ON tp.MaQuocGia = qg.MaQuocGia
                        LEFT JOIN HinhAnhTour a ON a.MaTour = t.MaTour AND a.LaAnhDaiDien = 1
                        LEFT JOIN (
                            SELECT MaTour,
                                   AVG(CAST(SoSao AS FLOAT)) AS SoSaoTrungBinh,
                                   COUNT(*) AS SoDanhGia
                            FROM DanhGia
                            GROUP BY MaTour
                        ) dg ON dg.MaTour = t.MaTour
                        LEFT JOIN (
                            SELECT tv.MaTour, MAX(v.GiaTri) AS GiamGia
                            FROM Tour_Voucher tv
                            JOIN Voucher v ON tv.MaVoucher = v.MaVoucher
                            WHERE v.NgayHetHan >= GETDATE()
                            GROUP BY tv.MaTour
                        ) v ON v.MaTour = t.MaTour
                        WHERE t.MaTour = :maTour
""", nativeQuery = true)
    Optional<TourDetailProjection> getTourDetail(@Param("maTour") Integer maTour);

    @Query(value = """
SELECT
    T.MaTour AS maTour,
    T.TenTour AS tenTour,
    (
        SELECT STRING_AGG(x.TenLoai, ', ')
        FROM (
            SELECT DISTINCT lt2.TenLoai
            FROM Tour_LoaiTour tlt2
            JOIN LoaiTour lt2 ON tlt2.MaLoaiTour = lt2.MaLoaiTour
            WHERE tlt2.MaTour = T.MaTour
        ) x
    ) AS loaiTour,
    T.MoTa AS moTa,
    T.ThoiGian AS thoiGian,

    GiaNL.Gia AS gia,

    DG.SoSaoTrungBinh AS soSaoTrungBinh,
    DG.SoDanhGia AS soDanhGia,
    A.DuongDan AS duongDanAnhDaiDien,
    PT.PhuongTien AS phuongTiens,
    V.GiamGia AS giamGia,

    (
        SELECT STRING_AGG(CONVERT(varchar, LK.NgayKhoiHanh, 23), ',')
        FROM LichKhoiHanh LK
        WHERE LK.MaTour = T.MaTour
    ) AS lichKhoiHanhs

FROM TourXemGanDay LS
JOIN Tour T ON LS.MaTour = T.MaTour

LEFT JOIN Tour_LoaiTour TLT ON T.MaTour = TLT.MaTour
LEFT JOIN LoaiTour LT ON TLT.MaLoaiTour = LT.MaLoaiTour

OUTER APPLY (
    SELECT TOP 1 GLKH.Gia
    FROM LichKhoiHanh LK2
    JOIN GiaLichKhoiHanh GLKH
        ON LK2.MaLichKhoiHanh = GLKH.MaLichKhoiHanh
    WHERE LK2.MaTour = T.MaTour
      AND GLKH.LoaiHanhKhach = 'NguoiLon'
    ORDER BY LK2.NgayKhoiHanh ASC
) GiaNL

LEFT JOIN (
    SELECT MaTour,
           AVG(CAST(SoSao AS FLOAT)) AS SoSaoTrungBinh,
           COUNT(*) AS SoDanhGia
    FROM DanhGia
    GROUP BY MaTour
) DG ON DG.MaTour = T.MaTour

LEFT JOIN (
    SELECT TP.MaTour,
           STRING_AGG(PT.TenPhuongTien, ', ') AS PhuongTien
    FROM Tour_PhuongTien TP
    JOIN PhuongTien PT ON TP.MaPhuongTien = PT.MaPhuongTien
    GROUP BY TP.MaTour
) PT ON PT.MaTour = T.MaTour

LEFT JOIN (
    SELECT TV.MaTour,
           MAX(V.GiaTri) AS GiamGia
    FROM Tour_Voucher TV
    JOIN Voucher V ON TV.MaVoucher = V.MaVoucher
    WHERE V.NgayHetHan >= GETDATE()
    GROUP BY TV.MaTour
) V ON V.MaTour = T.MaTour

LEFT JOIN HinhAnhTour A
    ON A.MaTour = T.MaTour
   AND A.LaAnhDaiDien = 1

WHERE
    (
        (:maNguoiDung IS NOT NULL AND LS.MaNguoiDung = :maNguoiDung)
     OR (:maNguoiDung IS NULL AND LS.ClientId = :clientId)
    )

GROUP BY
    T.MaTour, T.TenTour, T.MoTa, T.ThoiGian,
    GiaNL.Gia,
    DG.SoSaoTrungBinh, DG.SoDanhGia,
    A.DuongDan,
    PT.PhuongTien,
    V.GiamGia

ORDER BY MAX(LS.ThoiGianXem) DESC
OFFSET 0 ROWS FETCH NEXT 6 ROWS ONLY
""", nativeQuery = true)
    List<TourCardProjection> getRecentTours(
            @Param("maNguoiDung") Integer maNguoiDung,
            @Param("clientId") String clientId
    );


}
