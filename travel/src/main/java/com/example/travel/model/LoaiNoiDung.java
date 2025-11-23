package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LoaiNoiDung")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoaiNoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLoaiNoiDung")
    private Integer maLoaiNoiDung;

    @Column(name = "TenLoai", nullable = false, length = 100)
    private String tenLoai;

    @Column(name = "MoTa", length = 255)
    private String moTa;
}
