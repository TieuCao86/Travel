package com.example.travel.repository;

import com.example.travel.model.LichKhoiHanh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LichKhoiHanhRepository
        extends JpaRepository<LichKhoiHanh, Integer> {

    @Query("""
        SELECT DISTINCT l
        FROM LichKhoiHanh l
        LEFT JOIN FETCH l.giaLichKhoiHanhs
        WHERE l.tour.maTour = :maTour
    """)
    List<LichKhoiHanh> findByTour(@Param("maTour") Integer maTour);
}
