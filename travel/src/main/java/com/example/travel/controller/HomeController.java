package com.example.travel.controller;

import com.example.travel.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private TourRepository tourRepository;

    @GetMapping("/")
    public String home(Model model) {
        return "pages/home";
    }

    @GetMapping("/tour")
    public String tour() {
        return "pages/tour";
    }
}
