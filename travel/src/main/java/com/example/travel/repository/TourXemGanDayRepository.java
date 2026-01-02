package com.example.travel.repository;

import com.example.travel.model.TourXemGanDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TourXemGanDayRepository extends JpaRepository<TourXemGanDay, Integer> {

    Optional<TourXemGanDay>
    findByTour_MaTourAndNguoiDung_MaNguoiDung(
            Integer maTour,
            Long maNguoiDung
    );

    Optional<TourXemGanDay>
    findByTour_MaTourAndClientId(
            Integer maTour,
            String clientId
    );
}

