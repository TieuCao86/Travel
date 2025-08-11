package com.example.travel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maTour;

    private String tenTour;
    private String loaiTour;

    @Lob
    private String moTa;

    private String thoiGian;

    @OneToMany(mappedBy = "tour")
    private List<LichTrinhChiTiet> lichTrinhList;

    @OneToMany(mappedBy = "tour")
    private List<DatTour> datTours;

    @OneToMany(mappedBy = "tour")
    private List<DanhGia> danhGiaList;

    @ManyToMany
    @JoinTable(
            name = "TourPhuongTien",
            joinColumns = @JoinColumn(name = "MaTour"),
            inverseJoinColumns = @JoinColumn(name = "MaPhuongTien")
    )
    private List<PhuongTien> phuongTiens;

    @OneToMany(mappedBy = "tour")
    private List<HinhAnhTour> hinhAnhTours;

    @ManyToMany
    @JoinTable(
            name = "TourVoucher",
            joinColumns = @JoinColumn(name = "ma_tour"),
            inverseJoinColumns = @JoinColumn(name = "ma_voucher")
    )
    private Set<Voucher> vouchers;
}
