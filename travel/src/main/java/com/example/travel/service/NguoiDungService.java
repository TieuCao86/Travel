package com.example.travel.service;

import com.example.travel.model.NguoiDung;
import com.example.travel.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NguoiDungService {

    private final NguoiDungRepository repo;

    public NguoiDung save(NguoiDung user) {
        return repo.save(user);
    }

    public boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }

    public NguoiDung findByEmailOrSoDienThoai(String email, String phone) {
        return repo.findByEmail(email).orElseGet(() -> repo.findBySoDienThoai(phone));
    }

    public Optional<NguoiDung> findByEmail(String email) {
        return repo.findByEmail(email);
    }
}


