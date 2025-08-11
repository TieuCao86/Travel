package com.example.travel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maNguoiDung;

    private String hoTen;

    @Column(unique = true)
    private String email;

    private String soDienThoai;
    private String matKhau;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String vaiTro;

    @OneToMany(mappedBy = "nguoiDung")
    private List<SoThichNguoiDung> soThich;

    @OneToMany(mappedBy = "nguoiDung")
    private List<LichSuTimKiem> lichSuTimKiem;

    @OneToMany(mappedBy = "nguoiDung")
    private List<DatTour> datTours;

    @OneToMany(mappedBy = "nguoiDung")
    private List<Voucher> vouchers;

    @OneToMany(mappedBy = "nguoiDung")
    private List<DanhGia> danhGiaList;

    @OneToMany(mappedBy = "nguoiDung")
    private List<GoiYAI> goiYList;
}


