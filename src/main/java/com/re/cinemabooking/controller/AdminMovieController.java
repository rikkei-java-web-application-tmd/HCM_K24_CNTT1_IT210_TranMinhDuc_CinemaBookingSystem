package com.re.cinemabooking.controller;

import com.re.cinemabooking.dto.MovieFormDto;
import com.re.cinemabooking.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/movies")
@RequiredArgsConstructor
public class AdminMovieController {

    private final MovieService movieService;

    @GetMapping
    public String listMovies(Model model) {
        model.addAttribute("movies", movieService.findAll());
        return "admin/movie-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("movie", movieService.createForm());
        model.addAttribute("allGenres", movieService.findGenreOptions());
        return "admin/movie-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieService.findFormById(id));
        model.addAttribute("allGenres", movieService.findGenreOptions());
        return "admin/movie-form";
    }

    @PostMapping("/save")
    public String saveMovie(@ModelAttribute("movie") MovieFormDto movie, RedirectAttributes ra) {
        movieService.save(movie);
        ra.addFlashAttribute("successMessage", "Lưu thông tin phim thành công.");
        return "redirect:/admin/movies";
    }

    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id, RedirectAttributes ra) {
        movieService.delete(id);
        ra.addFlashAttribute("successMessage", "Đã xóa phim khỏi hệ thống.");
        return "redirect:/admin/movies";
    }
}
