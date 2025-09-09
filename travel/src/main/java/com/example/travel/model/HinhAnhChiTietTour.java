package com.example.travel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HinhAnhChiTietTour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maAnhChiTiet;

    @ManyToOne
    @JoinColumn(name = "maChiTiet")
    private LichTrinhChiTiet lichTrinhChiTiet;

    @Column(nullable = false)
    private String duongDan;

    private String moTa;

    @Column(columnDefinition = "BIT")
    private Boolean laAnhNoiBat = false;
}
