package com.example.travel.security;

import com.example.travel.model.NguoiDung;
import com.example.travel.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final NguoiDungService nguoiDungService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        NguoiDung user = nguoiDungService.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Email không tồn tại"));

        if (user.getMatKhau() == null) {
            throw new UsernameNotFoundException(
                    "Tài khoản không hỗ trợ đăng nhập local");
        }

        // Trả về CustomUserDetails thay vì User mặc định
        return new CustomUserDetails(
                user,
                nguoiDungService.getAuthorities(user.getMaNguoiDung())
        );
    }
}
