package com.example.travel.security;

import com.example.travel.model.NguoiDung;
import com.example.travel.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final NguoiDungService nguoiDungService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // Lấy email
        String email = (String) attributes.get("email");
        if (email == null) {
            throw new OAuth2AuthenticationException("Không lấy được email từ OAuth2 provider");
        }

        // Lấy tên
        String name = (String) attributes.get("name");

        // Kiểm tra user tồn tại
        NguoiDung user = nguoiDungService.findByEmail(email)
                .orElseGet(() -> {
                    NguoiDung nd = new NguoiDung();
                    nd.setEmail(email);
                    nd.setHoTen(name);
                    nd.setVaiTro("USER");
                    nd.setMatKhau("");
                    nd.setNgaySinh(LocalDate.now());
                    return nguoiDungService.save(nd);
                });

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                attributes,
                userNameAttributeName
        );
    }
}


