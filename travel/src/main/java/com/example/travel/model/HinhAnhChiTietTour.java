package com.example.travel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "HinhAnhChiTietTour")
public class HinhAnhChiTietTour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaAnhChiTiet")
    private Integer maAnhChiTiet;

    // FK tới LichTrinhChiTiet mới
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaChiTiet", nullable = false)
    private LichTrinhChiTiet lichTrinhChiTiet;

    @Column(name = "DuongDan", nullable = false)
    private String duongDan;

    @Column(name = "MoTa")
    private String moTa;

    @Column(name = "LaAnhNoiBat", columnDefinition = "BIT DEFAULT 0")
    private Boolean laAnhNoiBat = false;
}

