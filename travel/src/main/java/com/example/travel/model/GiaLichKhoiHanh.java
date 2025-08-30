package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiaTour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maGia;

    @ManyToOne
    @JoinColumn(name = "maTour")
    private Tour tour;

    private String loaiHanhKhach; // "NguoiLon", "TreEm", "TreNho"
    private Integer doTuoiTu;      // tuổi từ
    private Integer doTuoiDen;     // đến tuổi
    private BigDecimal gia;
    private String moTa;
}
