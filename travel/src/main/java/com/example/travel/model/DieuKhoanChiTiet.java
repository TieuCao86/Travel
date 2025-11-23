package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DieuKhoanChiTiet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DieuKhoanChiTiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaChiTiet")
    private Integer maChiTiet;

    @ManyToOne
    @JoinColumn(name = "MaDieuKhoan")
    private DieuKhoanTour dieuKhoanTour;

    @ManyToOne
    @JoinColumn(name = "MaLoaiNoiDung")
    private LoaiNoiDung loaiNoiDung;

    @Column(name = "TieuDe", length = 100)
    private String tieuDe;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;
}

