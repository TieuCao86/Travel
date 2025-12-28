package com.example.travel.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "TourXemGanDay",
        indexes = {
                @Index(name = "IX_XGD_USER_TIME", columnList = "MaNguoiDung, ThoiGianXem"),
                @Index(name = "IX_XGD_CLIENT_TIME", columnList = "ClientId, ThoiGianXem")
        }
)
@Data
public class TourXemGanDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaXem")
    private Integer maXem;

    /* ===== TOUR ===== */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTour", nullable = false)
    private Tour tour;

    /* ===== USER (nullable) ===== */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNguoiDung")
    private NguoiDung nguoiDung;

    /* ===== CLIENT ID (guest) ===== */
    @Column(name = "ClientId", length = 36)
    private String clientId;

    /* ===== VIEW TIME ===== */
    @Column(name = "ThoiGianXem", nullable = false)
    private LocalDateTime thoiGianXem;

    /* ===== AUTO SET TIME ===== */
    @PrePersist
    protected void onCreate() {
        if (thoiGianXem == null) {
            thoiGianXem = LocalDateTime.now();
        }
    }
}
