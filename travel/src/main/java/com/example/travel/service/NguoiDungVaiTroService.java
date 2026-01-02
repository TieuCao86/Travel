package com.example.travel.service;

import com.example.travel.model.NguoiDung;
import com.example.travel.model.NguoiDungVaiTro;
import com.example.travel.model.NguoiDungVaiTroId;
import com.example.travel.model.VaiTro;
import com.example.travel.repository.NguoiDungVaiTroRepository;
import com.example.travel.repository.VaiTroRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NguoiDungVaiTroService {

    private final NguoiDungVaiTroRepository nguoiDungVaiTroRepository;
    private final VaiTroRepository vaiTroRepository;
    private final EntityManager entityManager;

    public void addRoleUser(Long maNguoiDung) {

        VaiTro roleUser = vaiTroRepository
                .findByTenVaiTro("ROLE_USER")
                .orElseThrow(() ->
                        new RuntimeException("Chưa có ROLE_USER"));

        NguoiDungVaiTro ndvt = new NguoiDungVaiTro();
        ndvt.setId(new NguoiDungVaiTroId(
                maNguoiDung,
                roleUser.getMaVaiTro()
        ));

        // ✅ Lấy reference, KHÔNG query DB
        NguoiDung nguoiDungRef =
                entityManager.getReference(NguoiDung.class, maNguoiDung);

        ndvt.setNguoiDung(nguoiDungRef);
        ndvt.setVaiTro(roleUser);

        nguoiDungVaiTroRepository.save(ndvt);
    }
}

