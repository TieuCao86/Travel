package com.example.travel.repository;

import com.example.travel.model.DanhGia;
import com.example.travel.projection.DanhGiaProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {

    @Query("""
        SELECT d.nguoiDung.hoTen AS hoTen,
               d.soSao AS soSao,
               d.binhLuan AS binhLuan,
               d.ngayDanhGia AS ngayDanhGia
        FROM DanhGia d
        WHERE d.tour.maTour = :maTour
        ORDER BY d.ngayDanhGia DESC
    """)
    List<DanhGiaProjection> findDanhGiaByTour(@Param("maTour") Integer maTour, Pageable pageable);
}
