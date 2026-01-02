package com.example.travel.security;

import com.example.travel.model.NguoiDung;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends CustomUserDetails implements OAuth2User {

    private final Map<String, Object> attributes;

    public CustomOAuth2User(NguoiDung nguoiDung,
                            Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes) {
        super(nguoiDung, authorities);
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        // Đây là tên hiển thị cho OAuth2AuthenticationToken
        return getHoTen();
    }
}
