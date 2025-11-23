package com.example.travel.mapper;

import com.example.travel.model.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface VoucherMapper {

    @Named("mapGiamGia")
    default Double mapGiamGia(Set<Voucher> vouchers) {
        if (vouchers == null || vouchers.isEmpty()) return null;

        LocalDate now = LocalDate.now();

        return vouchers.stream()
                .filter(Objects::nonNull)
                .filter(v -> v.getTrangThai() == null || v.getTrangThai().equalsIgnoreCase("Hoạt động"))
                .filter(v -> v.getNgayHetHan() == null || !v.getNgayHetHan().isBefore(now))
                .map(Voucher::getGiaTri)
                .filter(Objects::nonNull)
                .mapToDouble(BigDecimal::doubleValue)
                .max()
                .orElse(0);
    }
}
