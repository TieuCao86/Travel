package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "DieuKhoanTour")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DieuKhoanTour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDieuKhoan")
    private Integer maDieuKhoan;

    @ManyToOne
    @JoinColumn(name = "MaTour")
    private Tour tour; // Giả sử bạn đã có entity Tour

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String giaBaoGom;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String giaKhongBaoGom;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String phuThu;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String visa;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String huyDoi;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String luuY;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String huongDanVien;

    @OneToMany(mappedBy = "dieuKhoanTour", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DieuKhoanChiTiet> danhSachChiTiet;
}

