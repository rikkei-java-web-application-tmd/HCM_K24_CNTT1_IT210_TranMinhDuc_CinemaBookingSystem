package com.re.cinemabooking.repository;

import com.re.cinemabooking.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findAllByOrderByNameAsc();
}
