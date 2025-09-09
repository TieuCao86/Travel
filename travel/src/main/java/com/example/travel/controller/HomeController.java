package com.example.travel.controller;

import com.example.travel.mapper.TourMapper;
import com.example.travel.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
                    model.addAttribute("tour", tourDetailDTO);
                    return "pages/tour-detail";
                })
                .orElse("error/404");
    }
}
