package com.example.travel.controller;

import com.example.travel.mapper.TourMapper;
import com.example.travel.service.TourService;
import lombok.RequiredArgsConstructor;
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
public class HomeController {

    private final TourService tourService;
    private final TourMapper tourMapper;

    @GetMapping("/")
    public String home(Model model) {
        return "pages/home";
    }

    @GetMapping("/tour")
    public String tour() {
        return "pages/tour";
    }

    @GetMapping("/tour/{id}")
    public String getTourDetail(@PathVariable("id") Integer id, Model model) {
        return tourService.getTourById(id)
                .map(tourDetailDTO -> {
                    String moTa = tourDetailDTO.getMoTa();
                    List<String> moTaList = List.of();

                    if (moTa != null && !moTa.isBlank()) {
                        // 1. Chuẩn hóa unicode
                        moTa = Normalizer.normalize(moTa, Normalizer.Form.NFKC);

                        // 2. Xử lý chuỗi literal "\\n" hoặc "\\r\\n" thành newline
                        moTa = moTa.replace("\\r\\n", "\n").replace("\\n", "\n");

                        // 3. Chuẩn hóa tất cả newline về '\n'
                        moTa = moTa.replace("\r\n", "\n").replace("\r", "\n");

                        // 4. Xóa ký tự ẩn (zero-width, BOM…)
                        moTa = moTa.replaceAll("[\\u200B\\u200C\\u200D\\uFEFF\\u00AD]", "");

                        // 5. Tách thành danh sách dòng
                        moTaList = Arrays.stream(moTa.split("\\n"))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .collect(Collectors.toList());
                    }

                    // Gắn vào model
                    model.addAttribute("tour", tourDetailDTO);
                    model.addAttribute("moTaList", moTaList); // dùng cho <ul><li>
                    model.addAttribute("moTaHtml", moTa == null ? "" : moTa.replace("\n", "<br/>")); // dùng cho <p>

                    return "pages/tour-detail";
                })
                .orElse("error/404");
    }
}
