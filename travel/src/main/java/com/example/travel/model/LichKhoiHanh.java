package com.example.travel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Tour tour;

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
    @JsonIgnore
    private ThanhPho thanhPhoKhoiHanh;

    @OneToMany(mappedBy = "lichKhoiHanh", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<GiaLichKhoiHanh> giaLichKhoiHanhs;
}
