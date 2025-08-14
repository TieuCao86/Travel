package com.example.travel.mapper;

import com.example.travel.dto.TourDTO;
import com.example.travel.model.DanhGia;
import com.example.travel.model.HinhAnhTour;
import com.example.travel.model.Tour;
import com.example.travel.model.PhuongTien;

import java.math.BigDecimal;
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
                            dto.setGiamGia(NumberFormat.getInstance(Locale.of("vi", "VN"))
                                    .format(v.getGiaTri()) + "₫");
                        }
                    });
        }

        return dto;
    }

    public static TourDTO fromRaw(Object[] row) {
        TourDTO dto = new TourDTO();
        dto.setMaTour((Integer) row[0]);
        dto.setTenTour((String) row[1]);
        dto.setLoaiTour((String) row[2]);
        dto.setMoTa((String) row[3]);
        dto.setThoiGian((String) row[4]);
        dto.setGia(row[5] != null ? (BigDecimal) row[5] : null);
        dto.setSoSaoTrungBinh(row[6] instanceof Double ? (Double) row[6] : 0.0);
        dto.setSoDanhGia(row[7] instanceof Integer ? (Integer) row[7] : 0);
        dto.setDuongDanAnhDaiDien((String) row[8]);

        String phuongTienStr = (String) row[9];
        dto.setPhuongTiens(phuongTienStr != null ? List.of(phuongTienStr.split(",\\s*")) : List.of());

        if (row[10] == null) {
            dto.setGiamGia(null);
        } else if (row[10] instanceof BigDecimal) {
            Locale localeVN = Locale.forLanguageTag("vi-VN");
            dto.setGiamGia(NumberFormat.getInstance(localeVN).format(row[10]) + "₫");
        } else {
            dto.setGiamGia(row[10].toString());
        }

        return dto;
    }

}

