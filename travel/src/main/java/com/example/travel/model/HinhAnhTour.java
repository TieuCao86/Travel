package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HinhAnhTour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maAnh;

    private String duongDan;

    private boolean laAnhDaiDien;

    @ManyToOne
    @JoinColumn(name = "maTour")
    private Tour tour;
}
