package com.example.travel.controller;

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

    @GetMapping("/")
    public String home(Model model) {
        return "pages/home";
    }

    @GetMapping("/tour")
    public String tour() {
        return "pages/tour";
    }

}
