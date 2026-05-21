package com.re.cinemabooking;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

import java.awt.Desktop;
import java.net.URI;

@SpringBootApplication
public class CinemaBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaBookingApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowserAfterStartup() {
        String homeUrl = "http://localhost:8080";
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create(homeUrl));
                System.out.println("====== Opened browser at " + homeUrl + " ======");
            } else {
                System.out.println("====== Browser auto-open is not supported. Open " + homeUrl + " manually. ======");
            }
        } catch (Exception e) {
            System.out.println("====== Could not auto-open browser. Open " + homeUrl + " manually. ======");
        }
    }

}
