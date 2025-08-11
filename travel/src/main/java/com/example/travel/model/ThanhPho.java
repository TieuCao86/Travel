package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ThanhPho")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThanhPho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaThanhPho")
    private Integer maThanhPho;

    @Column(name = "TenThanhPho", nullable = false, length = 100)
    private String tenThanhPho;

    @Column(name = "QuocGia", length = 100)
    private String quocGia;

    @Column(name = "DuongDanAnh", length = 200)
    private String duongDanAnh;
}
