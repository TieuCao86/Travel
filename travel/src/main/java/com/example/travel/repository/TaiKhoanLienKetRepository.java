package com.example.travel.repository;

import com.example.travel.model.TaiKhoanLienKet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaiKhoanLienKetRepository
        extends JpaRepository<TaiKhoanLienKet, Integer> {

    Optional<TaiKhoanLienKet>
    findByNhaCungCapAndProviderUserId(String nhaCungCap, String providerUserId);
}
