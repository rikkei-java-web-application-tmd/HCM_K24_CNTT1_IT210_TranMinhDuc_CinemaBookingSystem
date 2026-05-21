package com.re.cinemabooking.repository;

import com.re.cinemabooking.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
	List<Ticket> findByShowtimeId(Long showtimeId);

	@Query("""
			SELECT t
			FROM Ticket t
			JOIN t.booking b
			WHERE t.showtime.id = :showtimeId
			  AND t.seat.id IN :seatIds
			  AND b.status = 'PAID'
			""")
	List<Ticket> findActiveTicketsForSeats(@Param("showtimeId") Long showtimeId,
										  @Param("seatIds") List<Long> seatIds);

	@Query("""
			SELECT t
			FROM Ticket t
			JOIN FETCH t.seat
			JOIN t.booking b
			WHERE t.showtime.id = :showtimeId
			  AND b.status = 'PAID'
			""")
	List<Ticket> findActiveTicketsByShowtimeId(@Param("showtimeId") Long showtimeId);

	@Query("""
			SELECT COUNT(t.id)
			FROM Ticket t
			JOIN t.booking b
			WHERE t.showtime.id = :showtimeId
			  AND b.status = 'PAID'
			""")
	Long countActiveTicketsByShowtimeId(@Param("showtimeId") Long showtimeId);
}
