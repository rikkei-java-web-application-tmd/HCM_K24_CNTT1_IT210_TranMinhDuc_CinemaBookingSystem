package com.re.cinemabooking.controller;

import com.re.cinemabooking.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MovieRepository movieRepository;

    @GetMapping("/")
    public String homePage(Model model) {
        // Lấy toàn bộ phim từ Database gửi sang giao diện
        model.addAttribute("movies", movieRepository.findAll());
        return "index";
    }
}