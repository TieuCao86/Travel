package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "LichKhoiHanh")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichKhoiHanh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLichKhoiHanh")
    private Integer maLichKhoiHanh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTour", nullable = false)
    private Tour tour; // Quan hệ N-1 với Tour

    @Column(name = "NgayKhoiHanh", nullable = false)
    private LocalDate ngayKhoiHanh;

    @Column(name = "NgayKetThuc", nullable = false)
    private LocalDate ngayKetThuc;

    @Column(name = "HanChotDangKy", nullable = false)
    private LocalDate hanChotDangKy;

    @Column(name = "SoCho")
    private Integer soCho;

    @Column(name = "TrangThai", length = 20)
    private String trangThai = "Mở bán"; // default

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaThanhPhoKhoiHanh")
    private ThanhPho thanhPhoKhoiHanh;

    @OneToMany(mappedBy = "lichKhoiHanh", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GiaLichKhoiHanh> giaLichKhoiHanhs;
}
