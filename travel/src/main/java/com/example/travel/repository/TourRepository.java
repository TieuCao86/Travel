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
        SELECT
            MaTour,
            AVG(CAST(SoSao AS FLOAT)) AS SoSaoTrungBinh,
            COUNT(*) AS SoDanhGia
        FROM DanhGia
        GROUP BY MaTour
    ) DG ON DG.MaTour = T.MaTour

    LEFT JOIN (
        SELECT
            TP.MaTour,
            STRING_AGG(PT.TenPhuongTien, ', ') AS PhuongTien
        FROM TourPhuongTien TP
        JOIN PhuongTien PT
            ON TP.MaPhuongTien = PT.MaPhuongTien
        GROUP BY TP.MaTour
    ) PT ON PT.MaTour = T.MaTour

    LEFT JOIN (
        SELECT
            TV.MaTour,
            MAX(V.GiaTri) AS GiamGia
        FROM TourVoucher TV
        JOIN Voucher V
            ON TV.MaVoucher = V.MaVoucher
        WHERE V.NgayHetHan >= GETDATE()
        GROUP BY TV.MaTour
    ) V ON V.MaTour = T.MaTour

    LEFT JOIN HinhAnhTour A
        ON A.MaTour = T.MaTour
       AND A.LaAnhDaiDien = 1

    WHERE
        (:tenTour IS NULL OR T.TenTour LIKE CONCAT('%', :tenTour, '%'))
        AND (:loaiTour IS NULL OR T.LoaiTour = :loaiTour)
        AND (
            :thanhPho IS NULL OR EXISTS (
                SELECT 1
                FROM ThanhPho_Tour TP
                JOIN ThanhPho P
                    ON TP.MaThanhPho = P.MaThanhPho
                JOIN QuocGia Q
                    ON P.MaQuocGia = Q.MaQuocGia
                WHERE TP.MaTour = T.MaTour
                  AND (
                      P.TenThanhPho LIKE CONCAT('%', :thanhPho, '%')
                      OR Q.TenQuocGia LIKE CONCAT('%', :thanhPho, '%')
                  )
            )
        )
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
    List<TourCardProjection> getFullTourCards(
            @Param("tenTour") String tenTour,
            @Param("loaiTour") String loaiTour,
            @Param("thanhPho") String thanhPho,
            @Param("minGia") BigDecimal minGia,
            @Param("maxGia") BigDecimal maxGia,
            @Param("sort") String sort,
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

    @Query(
            value = """
       SELECT TOP 6
           t.MaTour AS maTour,
           t.TenTour AS tenTour,
           t.LoaiTour AS loaiTour,
           t.MoTa AS moTa,
           t.ThoiGian AS thoiGian,
           a.DuongDan AS duongDanAnhDaiDien,
           glkh.Gia AS gia,
           dg.SoSaoTrungBinh AS soSaoTrungBinh,
           dg.SoDanhGia AS soDanhGia
       FROM TourXemGanDay tx
       JOIN Tour t ON tx.MaTour = t.MaTour
       LEFT JOIN (
           SELECT MaTour, DuongDan
           FROM HinhAnhTour
           WHERE LaAnhDaiDien = 1
       ) a ON a.MaTour = t.MaTour
       LEFT JOIN (
           SELECT MaTour, Gia
           FROM (
               SELECT lk.MaTour, glkh.Gia,
                      ROW_NUMBER() OVER (PARTITION BY lk.MaTour ORDER BY lk.NgayKhoiHanh ASC) AS rn
               FROM LichKhoiHanh lk
               JOIN GiaLichKhoiHanh glkh ON lk.MaLichKhoiHanh = glkh.MaLichKhoiHanh
               WHERE glkh.LoaiHanhKhach = 'NguoiLon'
           ) t
           WHERE rn = 1
       ) glkh ON glkh.MaTour = t.MaTour
       LEFT JOIN (
           SELECT MaTour, AVG(CAST(SoSao AS FLOAT)) AS SoSaoTrungBinh, COUNT(*) AS SoDanhGia
           FROM DanhGia
           GROUP BY MaTour
       ) dg ON dg.MaTour = t.MaTour
       WHERE
            (:maNguoiDung IS NOT NULL AND tx.MaNguoiDung = :maNguoiDung)
         OR (:maNguoiDung IS NULL AND tx.SessionId = :sessionId)
       ORDER BY tx.ThoiGianXem DESC
     """,
            nativeQuery = true
    )
    List<TourCardProjection> findRecentTours(
            @Param("maNguoiDung") Integer maNguoiDung,
            @Param("sessionId") String sessionId
    );

    @Query(
            value = """
        SELECT TOP 3
            t.MaTour AS maTour,
            t.TenTour AS tenTour,
            t.LoaiTour AS loaiTour,
            t.MoTa AS moTa,
            t.ThoiGian AS thoiGian,
            a.DuongDan AS duongDanAnhDaiDien,
            glkh.Gia AS gia,
            ISNULL(dg.SoSaoTrungBinh, 0) AS soSaoTrungBinh,
            ISNULL(dg.SoDanhGia, 0) AS soDanhGia
        FROM Tour t

        -- Join để lấy Thành phố và Quốc gia của tour
        JOIN ThanhPho_Tour ttp ON t.MaTour = ttp.MaTour
        JOIN ThanhPho tp ON ttp.MaThanhPho = tp.MaThanhPho
        JOIN QuocGia qg ON tp.MaQuocGia = qg.MaQuocGia

        -- Ảnh đại diện
        LEFT JOIN (
            SELECT MaTour, DuongDan
            FROM HinhAnhTour
            WHERE LaAnhDaiDien = 1
        ) a ON a.MaTour = t.MaTour

        -- Giá rẻ nhất
        LEFT JOIN (
            SELECT MaTour, Gia
            FROM (
                SELECT lk.MaTour, glkh.Gia,
                       ROW_NUMBER() OVER (PARTITION BY lk.MaTour ORDER BY lk.NgayKhoiHanh ASC) AS rn
                FROM LichKhoiHanh lk
                JOIN GiaLichKhoiHanh glkh ON lk.MaLichKhoiHanh = glkh.MaLichKhoiHanh
                WHERE glkh.LoaiHanhKhach = 'NguoiLon'
            ) g
            WHERE rn = 1
        ) glkh ON glkh.MaTour = t.MaTour

        -- Rating
        LEFT JOIN (
            SELECT MaTour,
                   AVG(CAST(SoSao AS FLOAT)) AS SoSaoTrungBinh,
                   COUNT(*) AS SoDanhGia
            FROM DanhGia
            GROUP BY MaTour
        ) dg ON dg.MaTour = t.MaTour

        WHERE t.MaTour <> :maTour

          AND (
                -- Nếu nội địa → so sánh thành phố
                (:loaiTour = 'Nội địa' AND tp.TenThanhPho = :tenThanhPho)

                OR

                -- Nếu nước ngoài → so sánh quốc gia
                (:loaiTour <> 'Nội địa' AND qg.TenQuocGia = :tenQuocGia)
          )

        ORDER BY
            SoSaoTrungBinh DESC,
            SoDanhGia DESC
        """,
            nativeQuery = true
    )
    List<TourCardProjection> findRelatedTours(
            @Param("maTour") Integer maTour,
            @Param("loaiTour") String loaiTour,
            @Param("tenThanhPho") String tenThanhPho,
            @Param("tenQuocGia") String tenQuocGia
    );

}
