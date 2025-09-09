package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "GiaLichKhoiHanh")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiaLichKhoiHanh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaGia")
    private Integer maGia;

    @Column(name = "LoaiHanhKhach", nullable = false)
    private String loaiHanhKhach; // 'NguoiLon', 'TreEm', 'TreNho'

    @Column(name = "Gia", nullable = false)
    private Double gia;

    @Column(name = "DoTuoiTu")
    private Integer doTuoiTu;

    @Column(name = "DoTuoiDen")
    private Integer doTuoiDen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaLichKhoiHanh")
    private LichKhoiHanh lichKhoiHanh;
}

