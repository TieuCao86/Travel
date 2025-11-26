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
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Lấy thông tin user từ OAuth2 provider
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google / facebook
        String email = getEmail(attributes, registrationId);
        String name = getName(attributes, registrationId);

        // Kiểm tra user đã tồn tại trong DB chưa
        Optional<NguoiDung> optionalUser = nguoiDungService.findByEmail(email);
        NguoiDung user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            // Nếu chưa có thì tạo mới
            user = new NguoiDung();
            user.setEmail(email);
            user.setHoTen(name);
            user.setVaiTro("USER");
            user.setMatKhau(""); // OAuth2 login nên không cần mật khẩu
            user.setNgaySinh(LocalDate.now()); // tạm thời
            nguoiDungService.save(user);
        }

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                attributes,
                "email" // key dùng để lấy username/email
        );
    }

    private String getEmail(Map<String, Object> attributes, String registrationId) {
        if (registrationId.equals("google")) {
            return (String) attributes.get("email");
        } else if (registrationId.equals("facebook")) {
            return (String) attributes.get("email");
        }
        return null;
    }

    private String getName(Map<String, Object> attributes, String registrationId) {
        if (registrationId.equals("google")) {
            return (String) attributes.get("name");
        } else if (registrationId.equals("facebook")) {
            return (String) attributes.get("name");
        }
        return "Unknown";
    }
}
