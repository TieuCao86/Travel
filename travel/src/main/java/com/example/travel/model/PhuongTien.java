package com.example.travel.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class PhuongTien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maPhuongTien;

    private String tenPhuongTien;

    @ManyToMany(mappedBy = "phuongTiens")
    private List<Tour> tours;
}

