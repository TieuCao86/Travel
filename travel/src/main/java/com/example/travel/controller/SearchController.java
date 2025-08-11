package com.example.travel.controller;

import com.example.travel.model.Tour;
import com.example.travel.service.TourService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final TourService tourService;

    public SearchController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("/{type}")
    public String search(@PathVariable String type,
                         @RequestParam(value = "q", required = false) String keyword,
                         Model model) {

        model.addAttribute("keyword", keyword);
        model.addAttribute("type", type);

        switch (type.toLowerCase()) {
            case "tour":
                List<Tour> tours = tourService.searchTours(keyword);
                model.addAttribute("results", tours);
                break;
            case "hotel":
                // TODO: gọi HotelService.searchHotels(keyword)
                model.addAttribute("results", List.of());
                break;
            case "ticket":
                // TODO: gọi TicketService.searchTickets(keyword)
                model.addAttribute("results", List.of());
                break;
            case "activity":
                // TODO: gọi ActivityService.searchActivities(keyword)
                model.addAttribute("results", List.of());
                break;
            default:
                model.addAttribute("results", List.of());
        }

        return "search"; // View templates/searchResults.html
    }
}
