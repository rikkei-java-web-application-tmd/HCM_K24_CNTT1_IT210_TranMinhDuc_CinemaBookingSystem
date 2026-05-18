package com.re.cinemabooking.repository;

import com.re.cinemabooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByUserIdOrderByBookingDateDesc(Long userId);
}
