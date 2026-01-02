package com.example.travel.controller;

import com.example.travel.model.NguoiDung;
import com.example.travel.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final NguoiDungService nguoiDungService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "pages/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new NguoiDung());
        return "pages/register";
    }

    @PostMapping("/register")
    public String registerSubmit(
            @ModelAttribute("user") NguoiDung user,
            Model model
    ) {
        if (nguoiDungService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "pages/register";
        }

        user.setMatKhau(passwordEncoder.encode(user.getMatKhau()));

        nguoiDungService.save(user);

        return "redirect:/login";
    }
}

