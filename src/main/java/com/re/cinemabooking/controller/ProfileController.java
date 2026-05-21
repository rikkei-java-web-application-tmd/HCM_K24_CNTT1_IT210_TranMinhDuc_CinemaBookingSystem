package com.re.cinemabooking.controller;

import com.re.cinemabooking.dto.ProfileDto;
import com.re.cinemabooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public String profile(Authentication authentication, Model model) {
        if (!model.containsAttribute("profile")) {
            model.addAttribute("profile", userService.getProfile(authentication.getName()));
        }
        return "profile/profile";
    }

    @PostMapping
    public String updateProfile(@ModelAttribute("profile") ProfileDto profile,
                                Authentication authentication,
                                RedirectAttributes ra) {
        try {
            userService.updateProfile(authentication.getName(), profile);
            ra.addFlashAttribute("successMessage", "Đã cập nhật hồ sơ cá nhân.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            ra.addFlashAttribute("profile", profile);
        }
        return "redirect:/profile";
    }
}
