package com.example.travel.controller;

import com.example.travel.model.NguoiDung;
import com.example.travel.service.TourService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @GetMapping("/tour/{id}")
    public String getTourDetail(
            @PathVariable("id") Integer id,
            Model model,
            HttpServletRequest request,
            @AuthenticationPrincipal NguoiDung nguoiDung
    ) {
        // Không dùng session để nhận diện người dùng
        Integer maNguoiDung = (nguoiDung != null) ? nguoiDung.getMaNguoiDung() : null;

        // Lấy clientId từ cookie/header nếu FE gửi
        String clientId = request.getHeader("X-Client-Id");
        if (clientId == null) {
            clientId = request.getParameter("clientId"); // fallback
        }

        System.out.println("maNguoiDung = " + maNguoiDung);
        System.out.println("clientId = " + clientId);

        // Lưu lịch sử xem
        tourService.saveRecent(id, maNguoiDung, clientId);

        return tourService.getTourById(id)
                .map(tourDetailDTO -> {
                    String moTa = tourDetailDTO.getMoTa();
                    List<String> moTaList = List.of();

                    if (moTa != null && !moTa.isBlank()) {
                        moTa = Normalizer.normalize(moTa, Normalizer.Form.NFKC);
                        moTa = moTa.replace("\r\n", "\n").replace("\r", "\n");
                        moTa = moTa.replaceAll("[\\u200B\\u200C\\u200D\\uFEFF\\u00AD]", "");
                        moTaList = Arrays.stream(moTa.split("\n"))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .collect(Collectors.toList());
                    }

                    model.addAttribute("tour", tourDetailDTO);
                    model.addAttribute("moTaList", moTaList);
                    model.addAttribute("moTaHtml", moTa == null ? "" : moTa.replace("\n", "<br/>"));

                    return "pages/tour-detail";
                })
                .orElse("error/404");
    }

}
