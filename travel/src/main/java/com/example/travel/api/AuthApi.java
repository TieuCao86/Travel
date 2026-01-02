package com.example.travel.api;

import com.example.travel.dto.LoginRequest;
import com.example.travel.dto.RegisterRequest;
import com.example.travel.model.NguoiDung;
import com.example.travel.model.TaiKhoanLienKet;
import com.example.travel.service.JwtService;
import com.example.travel.service.NguoiDungService;
import com.example.travel.service.NguoiDungVaiTroService;
import com.example.travel.service.TaiKhoanLienKetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthApi {

    private final NguoiDungService nguoiDungService;
    private final TaiKhoanLienKetService taiKhoanLienKetService;
    private final NguoiDungVaiTroService nguoiDungVaiTroService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (nguoiDungService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email đã tồn tại");
        }

        NguoiDung user = new NguoiDung();
        user.setHoTen(request.getHoTen());
        user.setEmail(request.getEmail());
        user.setDienThoai(request.getDienThoai());
        user.setMatKhau(passwordEncoder.encode(request.getMatKhau()));
        user.setTrangThai("Hoạt động");

        user = nguoiDungService.save(user);

        // ROLE_USER
        nguoiDungVaiTroService.addRoleUser(user.getMaNguoiDung());

        // LOCAL link
        TaiKhoanLienKet link = new TaiKhoanLienKet();
        link.setNguoiDung(user);
        link.setNhaCungCap("LOCAL");
        link.setProviderUserId(user.getEmail());
        link.setEmail(user.getEmail());
        link.setTenHienThi(user.getHoTen());

        taiKhoanLienKetService.save(link);

        return ResponseEntity.ok("Đăng ký thành công");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        var userOpt = nguoiDungService.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Sai email hoặc mật khẩu");
        }

        NguoiDung user = userOpt.get();

        if (user.getMatKhau() == null ||
                !passwordEncoder.matches(request.getMatKhau(), user.getMatKhau())) {
            return ResponseEntity.badRequest().body("Sai email hoặc mật khẩu");
        }

        // Tạo JWT
        String token = jwtService.generateToken(new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getMatKhau(),
                List.of() // roles có thể thêm sau
        ));

        // Trả về token
        return ResponseEntity.ok(Map.of(
                "message", "Đăng nhập thành công",
                "token", token
        ));
    }


}
