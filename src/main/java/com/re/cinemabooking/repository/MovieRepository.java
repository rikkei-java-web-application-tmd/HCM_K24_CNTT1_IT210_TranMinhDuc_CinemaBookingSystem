package com.re.cinemabooking.repository;

import com.re.cinemabooking.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByStatusOrderByTitleAsc(String status);

    List<Movie> findAllByOrderByTitleAsc();
}
