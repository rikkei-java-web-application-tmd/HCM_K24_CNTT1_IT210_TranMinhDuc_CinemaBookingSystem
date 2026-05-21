package com.re.cinemabooking.service;

import com.re.cinemabooking.dto.BookingHistoryDto;
import com.re.cinemabooking.dto.BookingRequestDto;
import com.re.cinemabooking.dto.BookingTicketRowDto;
import com.re.cinemabooking.dto.SeatMapDto;
import com.re.cinemabooking.dto.SeatStatusDto;
import com.re.cinemabooking.entity.Booking;
import com.re.cinemabooking.entity.Seat;
import com.re.cinemabooking.entity.Showtime;
import com.re.cinemabooking.entity.Ticket;
import com.re.cinemabooking.entity.User;
import com.re.cinemabooking.repository.BookingRepository;
import com.re.cinemabooking.repository.SeatRepository;
import com.re.cinemabooking.repository.ShowtimeRepository;
import com.re.cinemabooking.repository.TicketRepository;
import com.re.cinemabooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private static final String STATUS_PAID = "PAID";
    private static final String STATUS_CANCELLED = "CANCELLED";
    private static final double TICKET_PRICE = 75000D;
    private static final int CANCELLATION_LIMIT_HOURS = 24;

    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public SeatMapDto getSeatMap(Long showtimeId) {
        Showtime showtime = showtimeRepository.findDetailedById(showtimeId)
                .orElseThrow(() -> new IllegalArgumentException("Suất chiếu không tồn tại."));
        List<Seat> seats = seatRepository.findByRoomIdOrderBySeatNameAsc(showtime.getRoom().getId());
        Set<Long> soldSeatIds = ticketRepository.findActiveTicketsByShowtimeId(showtimeId)
                .stream()
                .map(ticket -> ticket.getSeat().getId())
                .collect(Collectors.toSet());

        SeatMapDto dto = new SeatMapDto();
        dto.setShowtimeId(showtime.getId());
        dto.setMovieTitle(showtime.getMovie().getTitle());
        dto.setRoomName(showtime.getRoom().getName());
        dto.setStartTime(showtime.getStartTime());
        dto.setTotalSeats(showtime.getRoom().getTotalSeats());
        dto.setSoldSeats((long) soldSeatIds.size());
        dto.setSoldOut(dto.getTotalSeats() != null && soldSeatIds.size() >= dto.getTotalSeats());
        dto.setSeats(seats.stream()
                .map(seat -> new SeatStatusDto(seat.getId(), seat.getSeatName(), soldSeatIds.contains(seat.getId())))
                .toList());
        return dto;
    }

    @Transactional
    public Long bookTickets(String username, BookingRequestDto request) {
        if (request.getShowtimeId() == null) {
            throw new IllegalArgumentException("Thiếu thông tin suất chiếu.");
        }
        if (request.getSeatIds() == null || request.getSeatIds().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn ít nhất một ghế.");
        }

        User user = findUser(username);
        Showtime showtime = showtimeRepository.findLockedById(request.getShowtimeId())
                .orElseThrow(() -> new IllegalArgumentException("Suất chiếu không tồn tại."));

        if (showtime.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Suất chiếu này đã bắt đầu, không thể đặt vé.");
        }

        List<Long> seatIds = request.getSeatIds().stream().distinct().toList();
        List<Seat> seats = seatRepository.findByIdInAndRoomId(seatIds, showtime.getRoom().getId());
        if (seats.size() != seatIds.size()) {
            throw new IllegalArgumentException("Danh sách ghế không hợp lệ cho phòng chiếu này.");
        }

        List<Ticket> takenTickets = ticketRepository.findActiveTicketsForSeats(showtime.getId(), seatIds);
        if (!takenTickets.isEmpty()) {
            String takenSeatNames = takenTickets.stream()
                    .map(ticket -> ticket.getSeat().getSeatName())
                    .sorted()
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Ghế " + takenSeatNames + " vừa được người khác mua. Vui lòng chọn ghế khác.");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(STATUS_PAID);
        booking.setTotalAmount(TICKET_PRICE * seats.size());

        List<Ticket> tickets = seats.stream()
                .sorted(Comparator.comparing(Seat::getSeatName))
                .map(seat -> new Ticket(null, booking, showtime, seat, TICKET_PRICE))
                .collect(Collectors.toCollection(ArrayList::new));
        booking.setTickets(tickets);

        Booking savedBooking = bookingRepository.save(booking);
        return savedBooking.getId();
    }

    @Transactional(readOnly = true)
    public List<BookingHistoryDto> getHistory(String username) {
        User user = findUser(username);
        List<BookingTicketRowDto> rows = bookingRepository.findTicketHistoryRows(user.getId());
        Map<Long, BookingHistoryDto> historyByBooking = new LinkedHashMap<>();
        LocalDateTime cancellationCutoff = LocalDateTime.now().plusHours(CANCELLATION_LIMIT_HOURS);

        for (BookingTicketRowDto row : rows) {
            BookingHistoryDto dto = historyByBooking.computeIfAbsent(row.getBookingId(), bookingId -> {
                BookingHistoryDto item = new BookingHistoryDto();
                item.setBookingId(row.getBookingId());
                item.setBookingDate(row.getBookingDate());
                item.setTotalAmount(row.getTotalAmount());
                item.setStatus(row.getStatus());
                item.setMovieTitle(row.getMovieTitle());
                item.setStartTime(row.getStartTime());
                item.setRoomName(row.getRoomName());
                item.setCancellable(STATUS_PAID.equals(row.getStatus()) && row.getStartTime().isAfter(cancellationCutoff));
                return item;
            });
            dto.getSeatNames().add(row.getSeatName());
        }

        return new ArrayList<>(historyByBooking.values());
    }

    @Transactional
    public void cancelBooking(String username, Long bookingId) {
        User user = findUser(username);
        Booking booking = bookingRepository.findOwnedForUpdate(bookingId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn cần hủy."));

        if (!STATUS_PAID.equals(booking.getStatus())) {
            throw new IllegalArgumentException("Hóa đơn này không còn ở trạng thái có thể hủy.");
        }
        if (booking.getTickets() == null || booking.getTickets().isEmpty()) {
            throw new IllegalArgumentException("Hóa đơn không có vé để hủy.");
        }

        LocalDateTime startTime = booking.getTickets().get(0).getShowtime().getStartTime();
        if (!startTime.isAfter(LocalDateTime.now().plusHours(CANCELLATION_LIMIT_HOURS))) {
            throw new IllegalArgumentException("Chỉ được hủy vé trước giờ chiếu tối thiểu 24 tiếng.");
        }

        booking.setStatus(STATUS_CANCELLED);
        bookingRepository.save(booking);
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản."));
    }
}
