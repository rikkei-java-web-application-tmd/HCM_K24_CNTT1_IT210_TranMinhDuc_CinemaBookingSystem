package com.re.cinemabooking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(length = 1000)
	private String description;

	private String director;
	private Integer duration;
	private String posterUrl;
	private String status;

	@ManyToMany
	@JoinTable(
		name = "movie_genre",
		joinColumns = @JoinColumn(name = "movie_id"),
		inverseJoinColumns = @JoinColumn(name = "genre_id")
	)
	private List<Genre> genres;
}

