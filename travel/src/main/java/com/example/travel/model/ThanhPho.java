package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "ThanhPho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"quocGia", "tours"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ThanhPho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaThanhPho")
    @EqualsAndHashCode.Include
    private Integer maThanhPho;

    @Column(name = "TenThanhPho", nullable = false, length = 100)
    private String tenThanhPho;

    @Column(name = "MoTa", length = 200)
    private String moTa;

    @Column(name = "DuongDanAnh", nullable = false, length = 200)
    private String duongDanAnh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaQuocGia")
    private QuocGia quocGia;

    @ManyToMany(mappedBy = "thanhPhos")
    private Set<Tour> tours;

}
