package com.example.travel.repository;

import com.example.travel.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Integer> {

    List<Tour> findByTenTourContainingIgnoreCase(String keyword);

    @Query(value = """
    WITH DanhGiaCTE AS (
        SELECT MaTour,
               AVG(CAST(SoSao AS FLOAT)) AS SoSaoTrungBinh,
               COUNT(MaDanhGia) AS SoDanhGia
        FROM DanhGia
        GROUP BY MaTour
    ),
    PhuongTienCTE AS (
        SELECT MaTour,
               STRING_AGG(TenPhuongTien, ', ') AS PhuongTiens
        FROM (
            SELECT DISTINCT tp2.MaTour, pt.TenPhuongTien
            FROM TourPhuongTien tp2
            JOIN PhuongTien pt ON pt.MaPhuongTien = tp2.MaPhuongTien
        ) AS DistinctPT
        GROUP BY MaTour
    ),
    TopVoucherCTE AS (
        SELECT tv2.MaTour, v.LaPhanTram, v.GiaTri,
               ROW_NUMBER() OVER (
                   PARTITION BY tv2.MaTour
                   ORDER BY v.LaPhanTram DESC, v.GiaTri DESC
               ) AS RowNum
        FROM TourVoucher tv2
        JOIN Voucher v ON v.MaVoucher = tv2.MaVoucher
        WHERE v.TrangThai = N'Hoạt động'
          AND GETDATE() BETWEEN v.NgayBatDau AND v.NgayHetHan
    ),
    GiaTourCTE AS (
        SELECT MaTour,
               MAX(CASE WHEN LoaiHanhKhach = 'NguoiLon' THEN Gia ELSE NULL END) AS GiaNguoiLon
        FROM GiaTour
        GROUP BY MaTour
    )
    SELECT TOP 10
        t.MaTour,
        t.TenTour,
        t.LoaiTour,
        t.MoTa,
        t.ThoiGian,
        gt.GiaNguoiLon AS GiaNguoiLon,
        ISNULL(dg.SoSaoTrungBinh, 0) AS SoSaoTrungBinh,
        ISNULL(dg.SoDanhGia, 0) AS SoDanhGia,
        hat.DuongDan AS DuongDanAnhDaiDien,
        pt.PhuongTiens,
        tv.LaPhanTram,
        tv.GiaTri
    FROM Tour t
    LEFT JOIN DanhGiaCTE dg ON dg.MaTour = t.MaTour
    LEFT JOIN HinhAnhTour hat ON hat.MaTour = t.MaTour AND hat.LaAnhDaiDien = 1
    LEFT JOIN PhuongTienCTE pt ON pt.MaTour = t.MaTour
    LEFT JOIN TopVoucherCTE tv ON tv.MaTour = t.MaTour AND tv.RowNum = 1
    LEFT JOIN GiaTourCTE gt ON gt.MaTour = t.MaTour
    ORDER BY SoSaoTrungBinh DESC;
""", nativeQuery = true)
    List<Object[]> findTopTourWithHighestRating();

}
