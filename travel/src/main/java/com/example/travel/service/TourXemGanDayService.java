package com.example.travel.service;

import com.example.travel.model.TourXemGanDay;
import com.example.travel.repository.NguoiDungRepository;
import com.example.travel.repository.TourRepository;
import com.example.travel.repository.TourXemGanDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TourXemGanDayService {

    private final TourXemGanDayRepository repo;
    private final TourRepository tourRepository;
    private final NguoiDungRepository nguoiDungRepository;

    public void save(Long maNguoiDung, String clientId, Integer maTour) {

        TourXemGanDay tx;

        if (maNguoiDung != null) {
            // USER ĐÃ LOGIN
            tx = repo
                    .findByTour_MaTourAndNguoiDung_MaNguoiDung(maTour, maNguoiDung)
                    .orElseGet(TourXemGanDay::new);

            tx.setNguoiDung(
                    nguoiDungRepository.getReferenceById(maNguoiDung)
            );
            tx.setClientId(null);

        } else {
            // KHÁCH
            tx = repo
                    .findByTour_MaTourAndClientId(maTour, clientId)
                    .orElseGet(TourXemGanDay::new);

            tx.setNguoiDung(null);
            tx.setClientId(clientId);
        }

        tx.setTour(tourRepository.getReferenceById(maTour));
        // KHÔNG cần set thời gian nếu DB đã DEFAULT GETDATE()

        repo.save(tx);
    }
}

