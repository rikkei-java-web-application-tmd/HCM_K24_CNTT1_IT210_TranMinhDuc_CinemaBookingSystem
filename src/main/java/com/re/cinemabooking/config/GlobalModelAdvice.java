package com.re.cinemabooking.config;

import com.re.cinemabooking.dto.UserSummaryDto;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
public class GlobalModelAdvice {

    @ModelAttribute
    public void addSecurityFlags(Model model, Authentication authentication) {
        boolean isAuthenticated = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("isAdmin", false);
        model.addAttribute("isStaff", false);
        model.addAttribute("isCustomer", false);

        if (!isAuthenticated) {
            return;
        }

        String role = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse("CUSTOMER");

        model.addAttribute("currentUser", new UserSummaryDto(authentication.getName(), role));
        model.addAttribute("isAdmin", "ADMIN".equals(role));
        model.addAttribute("isStaff", "STAFF".equals(role));
        model.addAttribute("isCustomer", "CUSTOMER".equals(role));
    }
}
