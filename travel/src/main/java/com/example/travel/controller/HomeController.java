package com.example.travel.controller;

import com.example.travel.model.Tour;
import com.example.travel.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @Autowired
    private TourService tourService;

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
                .map(tour -> {
                    model.addAttribute("tour", tour);
                    return "pages/tour-detail"; // Thymeleaf template ở resources/templates/pages/tour-detail.html
                })
                .orElse("error/404"); // nếu không tìm thấy thì trả về trang lỗi 404
    }


}
