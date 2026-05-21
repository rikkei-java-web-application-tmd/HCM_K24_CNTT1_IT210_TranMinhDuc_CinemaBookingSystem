package com.re.cinemabooking.repository;

import com.re.cinemabooking.entity.Booking;
import com.re.cinemabooking.dto.BookingTicketRowDto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByUserIdOrderByBookingDateDesc(Long userId);

	@Query("""
			SELECT new com.re.cinemabooking.dto.BookingTicketRowDto(
				b.id, b.bookingDate, b.totalAmount, b.status,
				m.title, s.startTime, r.name, seat.seatName, t.price
			)
			FROM Ticket t
			JOIN t.booking b
			JOIN t.showtime s
			JOIN s.movie m
			JOIN s.room r
			JOIN t.seat seat
			WHERE b.user.id = :userId
			ORDER BY b.bookingDate DESC, b.id DESC, seat.seatName ASC
			""")
	List<BookingTicketRowDto> findTicketHistoryRows(@Param("userId") Long userId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("""
			SELECT DISTINCT b
			FROM Booking b
			LEFT JOIN FETCH b.tickets t
			LEFT JOIN FETCH t.showtime s
			LEFT JOIN FETCH s.movie
			LEFT JOIN FETCH s.room
			WHERE b.id = :bookingId
			  AND b.user.id = :userId
			""")
	Optional<Booking> findOwnedForUpdate(@Param("bookingId") Long bookingId,
										 @Param("userId") Long userId);
}
