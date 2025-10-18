package com.example.travel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maThanhToan;

    private String hinhThuc;
    private BigDecimal soTien;
    private LocalDateTime ngayThanhToan;
    private String maGiaoDich;

    @ManyToOne
    @JoinColumn(name = "maDatTour")
    private DatTour datTour;


}

