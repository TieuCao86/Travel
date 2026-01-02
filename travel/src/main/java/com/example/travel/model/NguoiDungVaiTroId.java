package com.example.travel.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDungVaiTroId implements Serializable {

    @Column(name = "MaNguoiDung")
    private Long maNguoiDung;

    @Column(name = "MaVaiTro")
    private Long maVaiTro;
}

