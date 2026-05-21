package com.re.cinemabooking.repository;

import com.re.cinemabooking.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAllByOrderByNameAsc();

    Optional<Room> findByName(String name);
}
