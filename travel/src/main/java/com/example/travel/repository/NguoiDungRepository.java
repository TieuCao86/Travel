package com.example.travel.repository;

import com.example.travel.model.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {

    boolean existsByEmail(String email);

    Optional<NguoiDung> findByEmail(String email);

    NguoiDung findBySoDienThoai(String phone);
}

