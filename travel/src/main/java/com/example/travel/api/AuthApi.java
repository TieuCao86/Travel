package com.example.travel.api;

import com.example.travel.model.NguoiDung;
import com.example.travel.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthApi {

    private final NguoiDungService nguoiDungService;

    // ============================
    // 🔥 API REGISTER
    // ============================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody NguoiDung user) {

        if (nguoiDungService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email đã tồn tại!");
        }

        user.setVaiTro("USER");
        nguoiDungService.save(user);

        return ResponseEntity.ok("Đăng ký thành công!");
    }

    // ============================
    // 🔥 API LOGIN
    // ============================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody NguoiDung request) {

        var user = nguoiDungService.findByEmailOrSoDienThoai(
                request.getEmail(),
                request.getSoDienThoai()
        );

        if (user == null || !user.getMatKhau().equals(request.getMatKhau())) {
            return ResponseEntity.badRequest().body("Sai tài khoản hoặc mật khẩu!");
        }

        return ResponseEntity.ok("Đăng nhập thành công!");
    }

}
