package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maTour;

    private String tenTour;

    @Lob
    private String moTa;

    private String thoiGian;

    /* ===== LOẠI TOUR (QUỐC NỘI / QUỐC TẾ) ===== */
    @ManyToMany
    @JoinTable(
            name = "Tour_LoaiTour",
            joinColumns = @JoinColumn(name = "MaTour"),
            inverseJoinColumns = @JoinColumn(name = "MaLoaiTour")
    )
    private Set<LoaiTour> loaiTours;

    /* ===== CÁC QUAN HỆ KHÁC (OK) ===== */
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LichTrinhNgay> lichTrinhNgayList;

    @OneToMany(mappedBy = "tour")
    private List<DatTour> datTours;

    @OneToMany(mappedBy = "tour")
    private Set<DanhGia> danhGiaList;

    @ManyToMany
    @JoinTable(
            name = "TourPhuongTien",
            joinColumns = @JoinColumn(name = "MaTour"),
            inverseJoinColumns = @JoinColumn(name = "MaPhuongTien")
    )
    private List<PhuongTien> phuongTiens;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HinhAnhTour> hinhAnhTourList;

    @OneToMany(mappedBy = "tour")
    private List<LichKhoiHanh> lichKhoiHanhs;

    @ManyToMany
    @JoinTable(
            name = "TourVoucher",
            joinColumns = @JoinColumn(name = "MaTour"),
            inverseJoinColumns = @JoinColumn(name = "MaVoucher")
    )
    private Set<Voucher> vouchers;

    @ManyToMany
    @JoinTable(
            name = "ThanhPho_Tour",
            schema = "dbo",
            joinColumns = @JoinColumn(name = "MaTour"),
            inverseJoinColumns = @JoinColumn(name = "MaThanhPho")
    )
    private Set<ThanhPho> thanhPhos;
}

