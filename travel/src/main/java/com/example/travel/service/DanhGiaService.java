package com.example.travel.service;

import com.example.travel.projection.DanhGiaProjection;
import com.example.travel.repository.DanhGiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DanhGiaService {

    private final DanhGiaRepository danhGiaRepository;

    public List<DanhGiaProjection> getTopDanhGia(Integer maTour, int limit) {
        return danhGiaRepository.findDanhGiaByTour(maTour, PageRequest.of(0, limit));
    }
}
