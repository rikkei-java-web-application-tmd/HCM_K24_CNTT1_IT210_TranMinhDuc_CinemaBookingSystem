package com.re.cinemabooking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private LocalDateTime bookingDate;
	private Double totalAmount;

	@Column(nullable = false, length = 30)
	private String status;

	@OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
	private List<Ticket> tickets;
}

