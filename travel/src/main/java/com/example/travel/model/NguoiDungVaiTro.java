package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "NguoiDung_VaiTro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDungVaiTro {

    @EmbeddedId
    private NguoiDungVaiTroId id;

    @ManyToOne
    @MapsId("maNguoiDung")
    @JoinColumn(name = "MaNguoiDung")
    private NguoiDung nguoiDung;

    @ManyToOne
    @MapsId("maVaiTro")
    @JoinColumn(name = "MaVaiTro")
    private VaiTro vaiTro;
}
