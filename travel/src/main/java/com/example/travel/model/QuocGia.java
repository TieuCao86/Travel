package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "QuocGia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuocGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaQuocGia")
    private Integer maQuocGia;

    @Column(name = "TenQuocGia", nullable = false, length = 100)
    private String tenQuocGia;

    @Column(name = "MaISO", length = 10)
    private String maISO; // ví dụ: VN, CN, JP

    @Column(name = "ChauLuc", length = 50)
    private String chauLuc;

    @Column(name = "CoURL", length = 200)
    private String coURL; // link hình cờ quốc gia

    // 🔹 Quan hệ 1-N: Một quốc gia có nhiều thành phố
    @OneToMany(mappedBy = "quocGia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ThanhPho> thanhPhos;
}
