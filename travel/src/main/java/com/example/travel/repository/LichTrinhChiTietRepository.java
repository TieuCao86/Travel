package com.example.travel.repository;

import com.example.travel.dto.LichTrinhChiTietDTO;
import com.example.travel.model.LichTrinhChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichTrinhChiTietRepository
        extends JpaRepository<LichTrinhChiTiet, Integer> {

    @Query("""
    SELECT new com.example.travel.dto.LichTrinhChiTietDTO(
        ct.maChiTiet,
        ln.maNgay,
        ct.thuTu,
        ct.loaiNoiDung,
        ct.noiDung,
        ct.thanhPho.maThanhPho
    )
    FROM LichTrinhChiTiet ct
    JOIN ct.lichTrinhNgay ln
    WHERE ln.tour.maTour = :maTour
    ORDER BY ln.ngayThu, ct.thuTu
""")
    List<LichTrinhChiTietDTO> findByTour(@Param("maTour") Integer maTour);

}
