package com.example.travel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoiYAI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maGoiY;

    private String tieuDe;

    @Lob
    private String tomTat;

    private LocalDateTime ngayGoiY;
    private BigDecimal tongChiPhi;
    private Integer soNgay;
    private Float doChinhXac;

    @ManyToOne
    @JoinColumn(name = "maNguoiDung")
    private NguoiDung nguoiDung;

    @OneToMany(mappedBy = "goiY")
    private List<ChiTietGoiY> chiTietList;
}

