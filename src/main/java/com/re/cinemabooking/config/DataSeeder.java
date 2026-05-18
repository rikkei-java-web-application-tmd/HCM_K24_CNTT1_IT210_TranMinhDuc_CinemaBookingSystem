package com.re.cinemabooking.config;

import com.re.cinemabooking.entity.*;
import com.re.cinemabooking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;
    private final MovieRepository movieRepository;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {
            User admin = new User(null, "admin", "123456", "Quản trị viên", "admin@cinema.com", "0123456789", "ADMIN", null);
            userRepository.save(admin);
        }

        if (genreRepository.count() == 0) {
            Genre action = genreRepository.save(new Genre(null, "Hành động", null));
            Genre comedy = genreRepository.save(new Genre(null, "Hài kịch", null));
            Genre horror = genreRepository.save(new Genre(null, "Kinh dị", null));

            if (movieRepository.count() == 0) {
                Movie movie1 = new Movie(null, "Lật Mặt 7", "Một bộ phim cảm động về gia đình", "Lý Hải", 120, "https://m.media-amazon.com/images/M/MV5BMjA4MDk1Mzg2Nl5BMl5BanBnXkFtZTgwMTE2MTA0OTE@._V1_FMjpg_UX1000_.jpg", "ACTIVE", List.of(comedy, action));
                movieRepository.save(movie1);
            }
        }

        if (roomRepository.count() == 0) {
            Room room1 = roomRepository.save(new Room(null, "Phòng 1 (Standard)", 20, null));
            Room room2 = roomRepository.save(new Room(null, "Phòng 2 (IMAX)", 20, null));


            for (int i = 1; i <= 10; i++) {
                seatRepository.save(new Seat(null, "A" + i, room1));
                seatRepository.save(new Seat(null, "B" + i, room1));
            }
        }
        System.out.println("====== DỮ LIỆU MỒI ĐÃ ĐƯỢC NẠP THÀNH CÔNG! ======");
    }
}