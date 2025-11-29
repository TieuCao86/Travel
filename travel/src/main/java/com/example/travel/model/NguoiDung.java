package com.example.travel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    // OAuth2 / social login
    private String provider;      // local / google / facebook
    private String providerId;    // id từ OAuth2 provider
    private String avatar;        // link avatar

    // Trạng thái và quản lý
    private String trangThai = "ACTIVE"; // ACTIVE / INACTIVE / LOCKED
    private LocalDateTime ngayTao = LocalDateTime.now();
    private LocalDateTime ngayCapNhat = LocalDateTime.now();

    // Các quan hệ
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
