package com.re.cinemabooking.controller;

import com.re.cinemabooking.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ShowtimeService showtimeService;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("showtimes", showtimeService.findVisibleShowtimes());
        return "index";
    }
}
