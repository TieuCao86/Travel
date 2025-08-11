package com.example.travel.mapper;

import com.example.travel.dto.LichTrinhDTO;
import com.example.travel.dto.TourDTO;
import com.example.travel.model.DanhGia;
import com.example.travel.model.HinhAnhTour;
import com.example.travel.model.Tour;
import com.example.travel.model.PhuongTien;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TourMapper {

    public static TourDTO toDTO(Tour tour) {
        TourDTO dto = new TourDTO();
        dto.setMaTour(tour.getMaTour());
        dto.setTenTour(tour.getTenTour());
        dto.setLoaiTour(tour.getLoaiTour());
        dto.setMoTa(tour.getMoTa());
        dto.setThoiGian(tour.getThoiGian());

        // Phương tiện
        dto.setPhuongTiens(
                tour.getPhuongTiens().stream()
                        .map(PhuongTien::getTenPhuongTien)
                        .collect(Collectors.toList())
        );

        // Ảnh đại diện
        if (tour.getHinhAnhTours() != null) {
            tour.getHinhAnhTours().stream()
                    .filter(HinhAnhTour::isLaAnhDaiDien)
                    .findFirst()
                    .ifPresent(anh -> dto.setDuongDanAnhDaiDien(anh.getDuongDan()));
        }

        // Trung bình sao và số đánh giá
        Double trungBinh = tour.getDanhGiaList().stream()
                .mapToDouble(DanhGia::getSoSao)
                .average()
                .orElse(0.0);
        dto.setSoSaoTrungBinh(trungBinh);
        dto.setSoDanhGia(tour.getDanhGiaList() != null ? tour.getDanhGiaList().size() : 0);

        if (tour.getVouchers() != null && !tour.getVouchers().isEmpty()) {
            tour.getVouchers().stream()
                    .filter(v -> v.getTrangThai().equalsIgnoreCase("Hoạt động"))
                    .filter(v -> v.getNgayBatDau().isBefore(LocalDate.now()) && v.getNgayHetHan().isAfter(LocalDate.now()))
                    .findFirst()
                    .ifPresent(v -> {
                        if (Boolean.TRUE.equals(v.getLaPhanTram())) {
                            dto.setGiamGia(v.getGiaTri().intValue() + "%");
                        } else {
                            dto.setGiamGia(NumberFormat.getInstance(new Locale("vi", "VN"))
                                    .format(v.getGiaTri()) + "₫");
                        }
                    });
        }

        return dto;
    }

}

