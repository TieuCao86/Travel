package com.example.travel.repository;

import com.example.travel.model.NguoiDungVaiTro;
import com.example.travel.model.NguoiDungVaiTroId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NguoiDungVaiTroRepository
        extends JpaRepository<NguoiDungVaiTro, NguoiDungVaiTroId> {

    List<NguoiDungVaiTro> findByNguoiDung_MaNguoiDung(Long maNguoiDung);
}
