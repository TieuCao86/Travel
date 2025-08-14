package com.example.travel.service;

import com.example.travel.dto.TourDTO;
import com.example.travel.mapper.TourMapper;
import com.example.travel.model.Tour;
import com.example.travel.repository.TourRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TourService {

    private final TourRepository tourRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    // ========================= CRUD =========================

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public Optional<Tour> getTourById(Integer id) {
        return tourRepository.findById(id);
    }

    public Tour createTour(Tour tour) {
        return tourRepository.save(tour);
    }

    @Transactional
    public Tour updateTour(Integer id, Tour updatedTour) {
        return tourRepository.findById(id)
                .map(tour -> {
                    tour.setTenTour(updatedTour.getTenTour());
                    tour.setLoaiTour(updatedTour.getLoaiTour());
                    tour.setMoTa(updatedTour.getMoTa());
                    tour.setThoiGian(updatedTour.getThoiGian());
                    return tourRepository.save(tour);
                }).orElse(null);
    }

    public boolean deleteTour(Integer id) {
        if (tourRepository.existsById(id)) {
            tourRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ========================= Stored Procedure =========================

    public List<TourDTO> getTours(
            String tenTour,
            String loaiTour,
            BigDecimal minGia,
            BigDecimal maxGia,
            String sortBy,
            int offset,
            int limit
    ) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("sp_GetTours")
                .registerStoredProcedureParameter("TenTour", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("LoaiTour", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("MinGia", BigDecimal.class, ParameterMode.IN)
                .registerStoredProcedureParameter("MaxGia", BigDecimal.class, ParameterMode.IN)
                .registerStoredProcedureParameter("SortBy", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("Offset", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("Limit", Integer.class, ParameterMode.IN)
                .setParameter("TenTour", tenTour)
                .setParameter("LoaiTour", loaiTour)
                .setParameter("MinGia", minGia)
                .setParameter("MaxGia", maxGia)
                .setParameter("SortBy", sortBy)
                .setParameter("Offset", offset)
                .setParameter("Limit", limit);

        List<Object[]> result = query.getResultList();
        return result.stream()
                .map(TourMapper::fromRaw)
                .collect(Collectors.toList());
    }

    // Hàm tiện lợi
    public List<TourDTO> searchTours(String keyword, int offset, int limit) {
        return getTours(keyword, null, null, null, null, offset, limit);
    }

    public List<TourDTO> filterTours(String loaiTour, BigDecimal minGia, BigDecimal maxGia, String sortBy, int offset, int limit) {
        return getTours(null, loaiTour, minGia, maxGia, sortBy, offset, limit);
    }

    public List<TourDTO> getTopTourWithHighestRating(int limit) {
        return getTours(null, null, null, null, "rating", 0, limit);
    }
}
