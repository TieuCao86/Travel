package com.example.travel.repository;

import com.example.travel.model.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {

    Optional<NguoiDung> findByDienThoai(String dienThoai);

    Optional<NguoiDung> findByEmail(String email);

    boolean existsByEmail(String email);
}


