package com.example.travel.security;

import com.example.travel.model.NguoiDung;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {

    private final NguoiDung nguoiDung;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(NguoiDung nguoiDung,
                             Collection<? extends GrantedAuthority> authorities) {
        this.nguoiDung = nguoiDung;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return nguoiDung.getMatKhau();
    }

    @Override
    public String getUsername() {
        return nguoiDung.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Phương thức tiện lợi để lấy thông tin khác
    public String getHoTen() {
        return nguoiDung.getHoTen();
    }

    public Long getMaNguoiDung() {
        return nguoiDung.getMaNguoiDung();
    }
}
