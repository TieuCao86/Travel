package com.example.travel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ChiTietGoiY")
public class ChiTietGoiY {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaChiTiet")
    private Integer maChiTiet;

    @Column(name = "NgayThu")
    private Integer ngayThu;

    @Column(name = "TenHoatDong", length = 100)
    private String tenHoatDong;

    @Lob
    @Column(name = "MoTa")
    private String moTa;

    @Column(name = "ThoiGianBatDau")
    private LocalTime thoiGianBatDau;

    @Column(name = "ThoiGianKetThuc")
    private LocalTime thoiGianKetThuc;

    // Liên kết với gói gợi ý AI
    @ManyToOne
    @JoinColumn(name = "MaGoiY", nullable = false)
    private GoiYAI goiY;

    // Liên kết với thành phố
    @ManyToOne
    @JoinColumn(name = "MaThanhPho")
    private ThanhPho thanhPho;
}
