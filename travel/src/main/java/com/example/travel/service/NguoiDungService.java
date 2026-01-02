package com.example.travel.service;

import com.example.travel.model.NguoiDung;
import com.example.travel.repository.NguoiDungRepository;
import com.example.travel.repository.NguoiDungVaiTroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NguoiDungService {

    private final NguoiDungRepository nguoiDungRepository;
    private final NguoiDungVaiTroRepository nguoiDungVaiTroRepository;

    public boolean existsByEmail(String email) {
        return nguoiDungRepository.existsByEmail(email);
    }

    public Optional<NguoiDung> findByEmail(String email) {
        return nguoiDungRepository.findByEmail(email);
    }

    public Optional<NguoiDung> findByDienThoai(String dienThoai) {
        return nguoiDungRepository.findByDienThoai(dienThoai);
    }

    public NguoiDung save(NguoiDung user) {
        return nguoiDungRepository.save(user);
    }

    public List<GrantedAuthority> getAuthorities(Long maNguoiDung) {

        return nguoiDungVaiTroRepository
                .findByNguoiDung_MaNguoiDung(maNguoiDung)
                .stream()
                .map(vt -> new SimpleGrantedAuthority(
                        vt.getVaiTro().getTenVaiTro()
                ))
                .collect(Collectors.toList());
    }


}

