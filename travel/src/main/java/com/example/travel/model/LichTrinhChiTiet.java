package com.example.travel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LichTrinhChiTiet")
public class LichTrinhChiTiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaChiTiet")
    private Integer maChiTiet;

    @Column(name = "NgayThu")
    private Integer ngayThu;

    @Column(name = "Buoi", length = 10)
    private String buoi;

    @Column(name = "HoatDong", length = 200)
    private String hoatDong;

    // Liên kết với Tour
    @ManyToOne
    @JoinColumn(name = "MaTour", nullable = false)
    private Tour tour;

    // Liên kết với Thành Phố
    @ManyToOne
    @JoinColumn(name = "MaThanhPho")
    private ThanhPho thanhPho;
}
