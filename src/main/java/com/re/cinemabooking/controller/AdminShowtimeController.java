package com.re.cinemabooking.controller;

import com.re.cinemabooking.dto.ShowtimeFormDto;
import com.re.cinemabooking.service.MovieService;
import com.re.cinemabooking.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/showtimes")
@RequiredArgsConstructor
public class AdminShowtimeController {

    private final ShowtimeService showtimeService;
    private final MovieService movieService;

    @GetMapping
    public String listShowtimes(Model model) {
        model.addAttribute("showtimes", showtimeService.findAll());
        return "admin/showtime-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("showtime")) {
            model.addAttribute("showtime", new ShowtimeFormDto());
        }
        model.addAttribute("movies", movieService.findActiveMovieOptions());
        model.addAttribute("rooms", showtimeService.findRoomOptions());
        return "admin/showtime-form";
    }

    @PostMapping("/save")
    public String saveShowtime(@ModelAttribute("showtime") ShowtimeFormDto showtime, RedirectAttributes ra) {
        try {
            showtimeService.createShowtime(showtime);
            ra.addFlashAttribute("successMessage", "Đã xếp lịch chiếu thành công.");
            return "redirect:/admin/showtimes";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            ra.addFlashAttribute("showtime", showtime);
            return "redirect:/admin/showtimes/create";
        }
    }
}
