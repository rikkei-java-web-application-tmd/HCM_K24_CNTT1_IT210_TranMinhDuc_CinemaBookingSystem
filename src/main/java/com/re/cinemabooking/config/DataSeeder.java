package com.re.cinemabooking.config;

import com.re.cinemabooking.entity.Genre;
import com.re.cinemabooking.entity.Movie;
import com.re.cinemabooking.entity.Room;
import com.re.cinemabooking.entity.Seat;
import com.re.cinemabooking.entity.User;
import com.re.cinemabooking.repository.GenreRepository;
import com.re.cinemabooking.repository.MovieRepository;
import com.re.cinemabooking.repository.RoomRepository;
import com.re.cinemabooking.repository.SeatRepository;
import com.re.cinemabooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;
    private final MovieRepository movieRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        seedUsers();
        seedGenresAndMovie();
        seedRoomsAndSeats();
        System.out.println("====== Cinema seed data is ready ======");
    }

    private void seedUsers() {
        ensureUser("admin", "123456", "Quản trị viên", "admin@cinema.com", "0123456789", "ADMIN");
        ensureUser("staff", "123456", "Nhân viên rạp", "staff@cinema.com", "0123456790", "STAFF");
        ensureUser("customer", "123456", "Khách hàng mẫu", "customer@cinema.com", "0123456791", "CUSTOMER");
    }

    private void ensureUser(String username, String rawPassword, String fullName,
                            String email, String phone, String role) {
        userRepository.findByUsername(username).ifPresentOrElse(user -> {
            if (!user.getPassword().startsWith("$2")) {
                user.setPassword(passwordEncoder.encode(rawPassword));
                userRepository.save(user);
            }
        }, () -> userRepository.save(new User(
                null,
                username,
                passwordEncoder.encode(rawPassword),
                fullName,
                email,
                phone,
                role,
                null
        )));
    }

    private void seedGenresAndMovie() {
        if (genreRepository.count() == 0) {
            Genre action = genreRepository.save(new Genre(null, "Hành động", null));
            Genre comedy = genreRepository.save(new Genre(null, "Hài kịch", null));
            Genre horror = genreRepository.save(new Genre(null, "Kinh dị", null));
            genreRepository.save(new Genre(null, "Gia đình", null));

            if (movieRepository.count() == 0) {
                Movie movie = new Movie(
                        null,
                        "Lật Mặt 7",
                        "Một bộ phim cảm động về gia đình.",
                        "Lý Hải",
                        120,
                        "https://m.media-amazon.com/images/M/MV5BMjA4MDk1Mzg2Nl5BMl5BanBnXkFtZTgwMTE2MTA0OTE@._V1_FMjpg_UX1000_.jpg",
                        "ACTIVE",
                        List.of(comedy, action, horror)
                );
                movieRepository.save(movie);
            }
        }
    }

    private void seedRoomsAndSeats() {
        Room standard = roomRepository.findByName("Phòng 1 (Standard)")
                .orElseGet(() -> roomRepository.save(new Room(null, "Phòng 1 (Standard)", 20, null)));
        Room imax = roomRepository.findByName("Phòng 2 (IMAX)")
                .orElseGet(() -> roomRepository.save(new Room(null, "Phòng 2 (IMAX)", 20, null)));

        ensureSeats(standard);
        ensureSeats(imax);
    }

    private void ensureSeats(Room room) {
        if (!seatRepository.findByRoomId(room.getId()).isEmpty()) {
            return;
        }

        for (int i = 1; i <= 10; i++) {
            seatRepository.save(new Seat(null, "A" + i, room));
            seatRepository.save(new Seat(null, "B" + i, room));
        }
    }
}
