package com.example.travel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichSuTimKiem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maTimKiem;

    private String tuKhoa;
    private String diaDiem;
    private LocalDateTime ngayTim;
    private Integer soNgay;
    private BigDecimal nganSachTu;
    private BigDecimal nganSachDen;

    @ManyToOne
    @JoinColumn(name = "maNguoiDung")
    private NguoiDung nguoiDung;
}

