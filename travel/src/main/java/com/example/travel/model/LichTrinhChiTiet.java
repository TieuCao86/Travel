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
@Table(name = "LichTrinhChiTiet")
public class LichTrinhChiTiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaChiTiet")
    private Integer maChiTiet;

    // Liên kết với bảng LichTrinhNgay
    @ManyToOne
    @JoinColumn(name = "MaNgay", nullable = false)
    private LichTrinhNgay lichTrinhNgay;

    @Column(name = "ThuTu")
    private Integer thuTu; // để sắp xếp trong 1 ngày

    @Column(name = "LoaiNoiDung", length = 20)
    private String loaiNoiDung; // TEXT / IMAGE / OPTION

    @OneToMany(mappedBy = "lichTrinhChiTiet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HinhAnhChiTietTour> hinhAnhChiTietList;

    @Column(name = "NoiDung", columnDefinition = "NVARCHAR(MAX)")
    private String noiDung; // mô tả, link ảnh hoặc option

    // Liên kết với Thành Phố (có thể null)
    @ManyToOne
    @JoinColumn(name = "MaThanhPho")
    private ThanhPho thanhPho;

    @OneToMany(mappedBy = "lichTrinhChiTiet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HinhAnhChiTietTour> hinhAnhChiTietTours;

}
