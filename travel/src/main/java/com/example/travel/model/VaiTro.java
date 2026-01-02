package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "VaiTro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaiTro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaVaiTro")
    private Long maVaiTro;

    @Column(name = "TenVaiTro", nullable = false, unique = true)
    private String tenVaiTro; // USER, ADMIN, STAFF

    @OneToMany(mappedBy = "vaiTro")
    private List<NguoiDungVaiTro> nguoiDungVaiTros;
}
