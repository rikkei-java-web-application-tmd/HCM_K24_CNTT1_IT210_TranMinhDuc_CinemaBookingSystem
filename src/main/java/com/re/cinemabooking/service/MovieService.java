package com.re.cinemabooking.service;

import com.re.cinemabooking.dto.GenreOptionDto;
import com.re.cinemabooking.dto.MovieFormDto;
import com.re.cinemabooking.dto.MovieListDto;
import com.re.cinemabooking.dto.MovieOptionDto;
import com.re.cinemabooking.entity.Genre;
import com.re.cinemabooking.entity.Movie;
import com.re.cinemabooking.repository.GenreRepository;
import com.re.cinemabooking.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    @Transactional(readOnly = true)
    public List<MovieListDto> findAll() {
        return movieRepository.findAllByOrderByTitleAsc()
                .stream()
                .map(this::toListDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MovieOptionDto> findActiveMovieOptions() {
        return movieRepository.findByStatusOrderByTitleAsc("ACTIVE")
                .stream()
                .map(movie -> new MovieOptionDto(movie.getId(), movie.getTitle(), movie.getDuration()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GenreOptionDto> findGenreOptions() {
        return genreRepository.findAllByOrderByNameAsc()
                .stream()
                .map(genre -> new GenreOptionDto(genre.getId(), genre.getName()))
                .toList();
    }

    public MovieFormDto createForm() {
        return new MovieFormDto();
    }

    @Transactional(readOnly = true)
    public MovieFormDto findFormById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phim có ID: " + id));
        return toFormDto(movie);
    }

    @Transactional
    public void save(MovieFormDto dto) {
        Movie movie = dto.getId() == null
                ? new Movie()
                : movieRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phim có ID: " + dto.getId()));

        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getDescription());
        movie.setDirector(dto.getDirector());
        movie.setDuration(dto.getDuration());
        movie.setPosterUrl(dto.getPosterUrl());
        movie.setStatus(dto.getStatus() == null || dto.getStatus().isBlank() ? "ACTIVE" : dto.getStatus());

        List<Long> genreIds = dto.getGenreIds() == null ? List.of() : dto.getGenreIds();
        movie.setGenres(new ArrayList<>(genreRepository.findAllById(genreIds)));
        movieRepository.save(movie);
    }

    @Transactional
    public void delete(Long id) {
        movieRepository.deleteById(id);
    }

    private MovieListDto toListDto(Movie movie) {
        List<String> genreNames = movie.getGenres() == null
                ? List.of()
                : movie.getGenres().stream().map(Genre::getName).toList();
        return new MovieListDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getDuration(),
                movie.getPosterUrl(),
                movie.getStatus(),
                genreNames
        );
    }

    private MovieFormDto toFormDto(Movie movie) {
        MovieFormDto dto = new MovieFormDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDescription(movie.getDescription());
        dto.setDirector(movie.getDirector());
        dto.setDuration(movie.getDuration());
        dto.setPosterUrl(movie.getPosterUrl());
        dto.setStatus(movie.getStatus());
        dto.setGenreIds(movie.getGenres() == null
                ? new ArrayList<>()
                : movie.getGenres().stream().map(Genre::getId).collect(Collectors.toCollection(ArrayList::new)));
        return dto;
    }
}
