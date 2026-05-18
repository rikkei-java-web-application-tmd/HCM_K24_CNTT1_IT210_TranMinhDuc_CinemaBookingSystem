package com.re.cinemabooking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	private Integer totalSeats;

	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
	private List<Seat> seats;
}

