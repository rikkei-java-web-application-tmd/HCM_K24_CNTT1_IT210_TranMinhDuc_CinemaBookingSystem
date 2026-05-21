package com.re.cinemabooking.service;

import com.re.cinemabooking.dto.RoomOptionDto;
import com.re.cinemabooking.dto.ShowtimeCardDto;
import com.re.cinemabooking.dto.ShowtimeFormDto;
import com.re.cinemabooking.dto.ShowtimeListDto;
import com.re.cinemabooking.entity.Movie;
import com.re.cinemabooking.entity.Room;
import com.re.cinemabooking.entity.Showtime;
import com.re.cinemabooking.repository.MovieRepository;
import com.re.cinemabooking.repository.RoomRepository;
import com.re.cinemabooking.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowtimeService {

    private static final int CLEANUP_MINUTES = 15;

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public List<ShowtimeListDto> findAll() {
        return showtimeRepository.findAllForAdmin();
    }

    @Transactional(readOnly = true)
    public List<RoomOptionDto> findRoomOptions() {
        return roomRepository.findAllByOrderByNameAsc()
                .stream()
                .map(room -> new RoomOptionDto(room.getId(), room.getName(), room.getTotalSeats()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ShowtimeCardDto> findVisibleShowtimes() {
        LocalDateTime now = LocalDateTime.now();
        return showtimeRepository.findVisibleShowtimeCards(now)
                .stream()
                .filter(showtime -> !showtime.getStartTime().isBefore(now))
                .toList();
    }

    @Transactional
    public void createShowtime(ShowtimeFormDto dto) {
        if (dto.getMovieId() == null || dto.getRoomId() == null || dto.getStartTime() == null) {
            throw new IllegalArgumentException("Vui lòng chọn đầy đủ phim, phòng và giờ bắt đầu.");
        }

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException("Phim không tồn tại."));
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Phòng chiếu không tồn tại."));

        if (movie.getDuration() == null || movie.getDuration() <= 0) {
            throw new IllegalArgumentException("Thời lượng phim chưa hợp lệ.");
        }

        LocalDateTime endTime = dto.getStartTime().plusMinutes(movie.getDuration() + CLEANUP_MINUTES);
        List<Showtime> conflicts = showtimeRepository.findConflictingShowtimes(
                room.getId(),
                dto.getStartTime(),
                endTime
        );

        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("Phòng này đã có lịch chiếu chồng lên khoảng thời gian bạn chọn.");
        }

        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setRoom(room);
        showtime.setStartTime(dto.getStartTime());
        showtime.setEndTime(endTime);
        showtimeRepository.save(showtime);
    }
}
