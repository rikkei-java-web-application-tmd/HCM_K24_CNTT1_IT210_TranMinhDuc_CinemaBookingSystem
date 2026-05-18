package com.re.cinemabooking.repository;

import com.re.cinemabooking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
	List<Seat> findByRoomId(Long roomId);
}
