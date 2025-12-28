package com.example.travel.controller;

import com.example.travel.dto.TourCardDTO;
import com.example.travel.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/tour")
@RequiredArgsConstructor
public class SearchController {

    private final TourService tourService;

    @GetMapping("/filter")
    public String filterTours(
            @RequestParam(value = "tenTour", required = false) String tenTour,
            @RequestParam(value = "loaiTour", required = false) String loaiTour,
            @RequestParam(value = "minGia", required = false) BigDecimal minGia,
            @RequestParam(value = "maxGia", required = false) BigDecimal maxGia,
            Model model) {

        List<TourCardDTO> tours = tourService.searchTours(
                tenTour,
                loaiTour,
                minGia,
                maxGia,
                null,   // sort
                0,      // offset
                20      // limit
        );

        model.addAttribute("tours", tours);
        model.addAttribute("tenTour", tenTour);
        model.addAttribute("loaiTour", loaiTour);
        model.addAttribute("minGia", minGia);
        model.addAttribute("maxGia", maxGia);

        return "pages/tour-filter";
    }
}





