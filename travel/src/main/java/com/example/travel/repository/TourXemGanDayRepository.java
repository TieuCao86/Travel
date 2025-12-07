package com.example.travel.repository;

import com.example.travel.model.TourXemGanDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TourXemGanDayRepository extends JpaRepository<TourXemGanDay, Integer> {

    @Query("""
        SELECT tx FROM TourXemGanDay tx
        WHERE tx.tour.maTour = :maTour
          AND (
                (:maNguoiDung IS NOT NULL AND tx.nguoiDung.maNguoiDung = :maNguoiDung)
             OR (:maNguoiDung IS NULL AND tx.sessionId = :sessionId)
          )
    """)
    Optional<TourXemGanDay> findExisting(
            @Param("maTour") Integer maTour,
            @Param("maNguoiDung") Integer maNguoiDung,
            @Param("sessionId") String sessionId
    );
}

