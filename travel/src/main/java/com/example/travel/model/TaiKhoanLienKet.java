package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "TaiKhoanLienKet",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"NhaCungCap", "ProviderUserId"}
        )
)
@Data
public class TaiKhoanLienKet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLienKet")
    private Integer maLienKet;

    @ManyToOne
    @JoinColumn(name = "MaNguoiDung", nullable = false)
    private NguoiDung nguoiDung;

    @Column(name = "NhaCungCap", nullable = false)
    private String nhaCungCap; // LOCAL / GOOGLE

    @Column(name = "ProviderUserId")
    private String providerUserId; // email (LOCAL) / sub (GOOGLE)

    @Column(name = "Email")
    private String email;

    @Column(name = "TenHienThi")
    private String tenHienThi;

    @Column(name = "AnhDaiDien")
    private String anhDaiDien;

    @Column(name = "NgayLienKet")
    private LocalDateTime ngayLienKet;
}

