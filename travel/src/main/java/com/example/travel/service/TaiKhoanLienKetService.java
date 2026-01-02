package com.example.travel.service;

import com.example.travel.model.TaiKhoanLienKet;
import com.example.travel.repository.TaiKhoanLienKetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaiKhoanLienKetService {

    private final TaiKhoanLienKetRepository repository;

    public Optional<TaiKhoanLienKet>
    findByProviderAndProviderUserId(String provider, String providerUserId) {

        return repository.findByNhaCungCapAndProviderUserId(
                provider, providerUserId
        );
    }

    public TaiKhoanLienKet save(TaiKhoanLienKet link) {
        return repository.save(link);
    }
}
