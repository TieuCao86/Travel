package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "QuocGia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "thanhPhos")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class QuocGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaQuocGia")
    @EqualsAndHashCode.Include
    private Integer maQuocGia;

    @Column(name = "TenQuocGia", nullable = false, length = 100)
    private String tenQuocGia;

    @Column(name = "MaISO", length = 10)
    private String maISO;

    @Column(name = "ChauLuc", length = 50)
    private String chauLuc;

    @Column(name = "CoURL", length = 200)
    private String coURL;

    @OneToMany(mappedBy = "quocGia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ThanhPho> thanhPhos;
}
