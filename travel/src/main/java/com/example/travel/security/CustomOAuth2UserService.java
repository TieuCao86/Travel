package com.example.travel.security;

import com.example.travel.model.NguoiDung;
import com.example.travel.model.TaiKhoanLienKet;
import com.example.travel.service.NguoiDungService;
import com.example.travel.service.NguoiDungVaiTroService;
import com.example.travel.service.TaiKhoanLienKetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final NguoiDungService nguoiDungService;
    private final TaiKhoanLienKetService taiKhoanLienKetService;
    private final NguoiDungVaiTroService nguoiDungVaiTroService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String providerUserId;
        String email = null;
        String name = null;
        String avatar = null;

        switch (provider) {
            case "GOOGLE" -> {
                providerUserId = (String) attributes.get("sub");
                email = (String) attributes.get("email");
                name = (String) attributes.get("name");
                avatar = (String) attributes.get("picture");
            }
            case "FACEBOOK" -> {
                providerUserId = (String) attributes.get("id");
                email = (String) attributes.get("email");
                name = (String) attributes.get("name");
            }
            default -> throw new OAuth2AuthenticationException(
                    "Provider chưa được hỗ trợ: " + provider
            );
        }

        // 1️⃣ Tìm theo provider + providerUserId
        Optional<TaiKhoanLienKet> linkOpt =
                taiKhoanLienKetService.findByProviderAndProviderUserId(provider, providerUserId);

        NguoiDung user;

        if (linkOpt.isPresent()) {
            user = linkOpt.get().getNguoiDung();
        } else {
            // 2️⃣ Tìm user theo email
            user = (email != null) ? nguoiDungService.findByEmail(email).orElse(null) : null;

            // 3️⃣ Nếu chưa có user → tạo mới
            if (user == null) {
                user = new NguoiDung();
                user.setEmail(email);
                user.setHoTen(name != null ? name : "Người dùng OAuth");
                user.setTrangThai("Hoạt động");
                user = nguoiDungService.save(user);

                // Gán ROLE_USER mặc định
                nguoiDungVaiTroService.addRoleUser(user.getMaNguoiDung());
            }

            // 4️⃣ Lưu liên kết OAuth
            TaiKhoanLienKet link = new TaiKhoanLienKet();
            link.setNguoiDung(user);
            link.setNhaCungCap(provider);
            link.setProviderUserId(providerUserId);
            link.setEmail(email);
            link.setTenHienThi(name);
            link.setAnhDaiDien(avatar);

            taiKhoanLienKetService.save(link);
        }

        // 5️⃣ Trả về CustomOAuth2User
        return new CustomOAuth2User(
                user,
                nguoiDungService.getAuthorities(user.getMaNguoiDung()),
                attributes
        );
    }
}
