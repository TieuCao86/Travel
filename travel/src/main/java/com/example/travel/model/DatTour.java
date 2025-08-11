package com.example.travel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatTour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maDatTour;

    private LocalDateTime ngayDat;
    private Integer soNguoiLon;
    private Integer soTreEm;
    private BigDecimal tongTien;
    private String trangThai;

    @ManyToOne
    @JoinColumn(name = "maTour")
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "maNguoiDung")
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "maVoucher")
    private Voucher voucher;

    @OneToMany(mappedBy = "datTour")
    private List<ThanhToan> thanhToanList;
}

