package com.example.travel.repository;

import com.example.travel.model.Tour;
import com.example.travel.projection.TourCardProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Integer> {

    @Query(value = "EXEC sp_GetTours :TenTour, :LoaiTour, :MinGia, :MaxGia, :SortBy, :Offset, :Limit",
            nativeQuery = true)
    List<TourCardProjection> getTours(
            @Param("TenTour") String tenTour,
            @Param("LoaiTour") String loaiTour,
            @Param("MinGia") Double minGia,
            @Param("MaxGia") Double maxGia,
            @Param("SortBy") String sortBy,
            @Param("Offset") Integer offset,
            @Param("Limit") Integer limit
    );

    // Search by name
    @Query("SELECT t FROM Tour t WHERE LOWER(t.tenTour) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Tour> searchByName(@Param("q") String q, Pageable pageable);
}
