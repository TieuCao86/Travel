package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "NguoiDung")
@Data
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNguoiDung")
    private Long maNguoiDung;

    @Column(name = "HoTen", nullable = false)
    private String hoTen;

    @Column(name = "Email", unique = true)
    private String email;

    @Column(name = "MatKhau")
    private String matKhau; // LOCAL login

    @Column(name = "DienThoai")
    private String dienThoai;

    @Column(name = "TrangThai")
    private String trangThai;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @OneToMany(mappedBy = "nguoiDung")
    private List<TaiKhoanLienKet> taiKhoanLienKets;

    @OneToMany(mappedBy = "nguoiDung")
    private List<NguoiDungVaiTro> vaiTros;
}

