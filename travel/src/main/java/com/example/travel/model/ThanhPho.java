package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "ThanhPho")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThanhPho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaThanhPho")
    private Integer maThanhPho;

    @Column(name = "TenThanhPho", nullable = false, length = 100)
    private String tenThanhPho;

    @Column(name = "MoTa", length = 200)
    private String moTa;

    @Column(name = "DuongDanAnh", nullable = false, length = 200)
    private String duongDanAnh;

    // 🔹 Quan hệ N-1: Mỗi thành phố thuộc về một quốc gia
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaQuocGia") // Khóa ngoại
    private QuocGia quocGia;

    // 🔹 Quan hệ N-N với Tour (đã có)
    @ManyToMany(mappedBy = "thanhPhos")
    private Set<Tour> tours;
}
