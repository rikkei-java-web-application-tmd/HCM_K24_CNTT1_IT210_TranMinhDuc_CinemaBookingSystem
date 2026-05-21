package com.re.cinemabooking.repository;

import com.re.cinemabooking.entity.Showtime;
import com.re.cinemabooking.dto.ShowtimeCardDto;
import com.re.cinemabooking.dto.ShowtimeListDto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
	@Query("SELECT s FROM Showtime s WHERE s.room.id = :roomId " +
		   "AND s.startTime < :endTime AND s.endTime > :startTime")
	List<Showtime> findConflictingShowtimes(@Param("roomId") Long roomId,
										   @Param("startTime") LocalDateTime startTime,
										   @Param("endTime") LocalDateTime endTime);

	@Query("""
			SELECT new com.re.cinemabooking.dto.ShowtimeListDto(
				s.id, m.title, r.name, s.startTime, s.endTime
			)
			FROM Showtime s
			JOIN s.movie m
			JOIN s.room r
			ORDER BY s.startTime DESC
			""")
	List<ShowtimeListDto> findAllForAdmin();

	@Query("""
			SELECT new com.re.cinemabooking.dto.ShowtimeCardDto(
				s.id, m.title, m.posterUrl, m.duration, r.name, s.startTime,
				r.totalSeats, COUNT(t.id)
			)
			FROM Showtime s
			JOIN s.movie m
			JOIN s.room r
			LEFT JOIN Ticket t ON t.showtime = s AND t.booking.status = 'PAID'
			WHERE s.startTime >= :now
			  AND m.status = 'ACTIVE'
			GROUP BY s.id, m.title, m.posterUrl, m.duration, r.name, s.startTime, r.totalSeats
			ORDER BY s.startTime ASC
			""")
	List<ShowtimeCardDto> findVisibleShowtimeCards(@Param("now") LocalDateTime now);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT s FROM Showtime s JOIN FETCH s.movie JOIN FETCH s.room WHERE s.id = :id")
	Optional<Showtime> findLockedById(@Param("id") Long id);

	@Query("SELECT s FROM Showtime s JOIN FETCH s.movie JOIN FETCH s.room WHERE s.id = :id")
	Optional<Showtime> findDetailedById(@Param("id") Long id);
}
