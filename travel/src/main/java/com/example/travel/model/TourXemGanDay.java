package com.example.travel.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "TourXemGanDay")
@Data
public class TourXemGanDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maXem;

    @ManyToOne
    @JoinColumn(name = "MaTour")
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "MaNguoiDung", nullable = true)
    private NguoiDung nguoiDung;

    @Column(name = "SessionId", length = 100)
    private String sessionId;

    @Column(name = "ThoiGianXem")
    private LocalDateTime thoiGianXem;

}
