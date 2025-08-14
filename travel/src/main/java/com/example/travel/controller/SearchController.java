package com.example.travel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @GetMapping("/search/tour")
    public String searchTourPage(@RequestParam(name = "q", required = false) String query, Model model) {
        // Truyền giá trị query vào model để dùng trong HTML
        model.addAttribute("query", query);

        // TODO: Ở đây bạn có thể gọi service tìm kiếm tour theo query rồi thêm kết quả vào model nữa

        return "pages/searchResults"; // Trả về file searchResults.html
    }
}
