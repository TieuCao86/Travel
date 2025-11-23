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
    private String loaiTour;

    @Lob
    private String moTa;

    private String thoiGian;

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
            name = "ThanhPho_Tour",       // tên bảng trung gian đúng trong DB
            schema = "dbo",               // bắt buộc với SQL Server nếu DB có schema
            joinColumns = @JoinColumn(name = "MaTour"),
            inverseJoinColumns = @JoinColumn(name = "MaThanhPho")
    )
    private Set<ThanhPho> thanhPhos;

}
