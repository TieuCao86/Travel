package com.example.travel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maVoucher;

    @Column(unique = true)
    private String maCode;

    private String moTa;
    private BigDecimal giaTri;
    private Boolean laPhanTram;
    private BigDecimal giaTriToiDa;
    private BigDecimal donHangToiThieu;
    private LocalDate ngayBatDau;
    private LocalDate ngayHetHan;
    private Integer soLanSuDungToiDa;
    private Integer soLanDaSuDung;
    private String trangThai;

    @ManyToOne
    @JoinColumn(name = "maNguoiDung")
    private NguoiDung nguoiDung;

    @OneToMany(mappedBy = "voucher")
    private List<DatTour> datTourList;
}

