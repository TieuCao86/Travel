package com.example.travel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LichTrinhNgay")
public class LichTrinhNgay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNgay")
    private Integer maNgay;

    @Column(name = "NgayThu", nullable = false)
    private Integer ngayThu; // Ví dụ: Ngày 1, Ngày 2

    @Column(name = "TieuDe", length = 200)
    private String tieuDe; // Ví dụ: HCM - Singapore - Merlion Park

    @Column(name = "BuaAn", length = 100)
    private String buaAn; // Ví dụ: "Sáng, Trưa, Tối"

    @Column(name = "MoTaTongQuan", columnDefinition = "NVARCHAR(MAX)")
    private String moTaTongQuan; // Mô tả chi tiết trong ngày

    // Liên kết với Tour
    @ManyToOne
    @JoinColumn(name = "MaTour", nullable = false)
    private Tour tour;

    // Liên kết với LichTrinhChiTiet
    @OneToMany(mappedBy = "lichTrinhNgay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LichTrinhChiTiet> chiTietList;
}
