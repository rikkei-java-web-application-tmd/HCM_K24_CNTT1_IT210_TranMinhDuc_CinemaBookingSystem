package com.re.cinemabooking.controller;

import com.re.cinemabooking.dto.BookingRequestDto;
import com.re.cinemabooking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/showtimes/{showtimeId}/seats")
    public String seatMap(@PathVariable Long showtimeId, Model model) {
        model.addAttribute("seatMap", bookingService.getSeatMap(showtimeId));
        model.addAttribute("bookingRequest", new BookingRequestDto());
        return "booking/seat-map";
    }

    @PostMapping("/bookings")
    public String bookTickets(@ModelAttribute BookingRequestDto request,
                              Authentication authentication,
                              RedirectAttributes ra) {
        try {
            Long bookingId = bookingService.bookTickets(authentication.getName(), request);
            ra.addFlashAttribute("successMessage", "Đặt vé thành công. Mã hóa đơn: #" + bookingId);
            return "redirect:/bookings/history";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/showtimes/" + request.getShowtimeId() + "/seats";
        }
    }

    @GetMapping("/bookings/history")
    public String history(Authentication authentication, Model model) {
        model.addAttribute("bookings", bookingService.getHistory(authentication.getName()));
        return "booking/history";
    }

    @PostMapping("/bookings/{bookingId}/cancel")
    public String cancel(@PathVariable Long bookingId,
                         Authentication authentication,
                         RedirectAttributes ra) {
        try {
            bookingService.cancelBooking(authentication.getName(), bookingId);
            ra.addFlashAttribute("successMessage", "Đã hủy vé và giải phóng ghế thành công.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/bookings/history";
    }
}
