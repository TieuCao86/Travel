package com.example.travel.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DanhGiaDTO {

    private String hoTen;
    private Integer soSao;
    private String binhLuan;
    private LocalDateTime ngayDanhGia;
}


