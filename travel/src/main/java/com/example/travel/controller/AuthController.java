package com.example.travel.controller;

import com.example.travel.model.NguoiDung;
import com.example.travel.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final NguoiDungService nguoiDungService;

    // Trang đăng nhập
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new NguoiDung());
        return "pages/login";
    }

    // Xử lý đăng nhập
    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute("loginRequest") NguoiDung request, Model model) {

        NguoiDung user = nguoiDungService.findByEmailOrSoDienThoai(
                request.getEmail(),
                request.getSoDienThoai()
        );

        if (user == null || !user.getMatKhau().equals(request.getMatKhau())) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            return "pages/login";
        }

        // Đúng mật khẩu → chuyển về trang chủ
        return "redirect:/";
    }

    // Trang đăng ký
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new NguoiDung());
        return "pages/register";
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute("user") NguoiDung user, Model model) {

        // Kiểm tra email đã tồn tại
        if (nguoiDungService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "pages/register";
        }

        user.setVaiTro("USER"); // set role mặc định

        nguoiDungService.save(user);

        return "redirect:/login";
    }
}
