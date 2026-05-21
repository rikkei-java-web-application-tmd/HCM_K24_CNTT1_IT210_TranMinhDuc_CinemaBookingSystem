CREATE DATABASE IF NOT EXISTS cinema_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE cinema_db;

-- Run this file after starting the Spring Boot app once so Hibernate can create/update tables.
-- Password for admin/staff/customer is: 123456

INSERT IGNORE INTO users (id, username, password, full_name, email, phone, role) VALUES
(1, 'admin', '$2a$10$3SWt2.9SpfFWrY6t8UfH3OXJ9eCWy4tP2GGtg3xAvRPxJakBsR.7W', 'Quản trị viên', 'admin@cinema.com', '0123456789', 'ADMIN'),
(2, 'staff', '$2a$10$3SWt2.9SpfFWrY6t8UfH3OXJ9eCWy4tP2GGtg3xAvRPxJakBsR.7W', 'Nhân viên rạp', 'staff@cinema.com', '0123456790', 'STAFF'),
(3, 'customer', '$2a$10$3SWt2.9SpfFWrY6t8UfH3OXJ9eCWy4tP2GGtg3xAvRPxJakBsR.7W', 'Khách hàng mẫu', 'customer@cinema.com', '0123456791', 'CUSTOMER');

INSERT IGNORE INTO genres (id, name) VALUES
(1, 'Hành động'),
(2, 'Hài kịch'),
(3, 'Kinh dị'),
(4, 'Gia đình');

INSERT IGNORE INTO movies (id, title, description, director, duration, poster_url, status) VALUES
(1,
 'Lật Mặt 7',
 'Một bộ phim cảm động về gia đình, dùng làm dữ liệu mẫu cho demo đặt vé.',
 'Lý Hải',
 120,
 'https://m.media-amazon.com/images/M/MV5BMjA4MDk1Mzg2Nl5BMl5BanBnXkFtZTgwMTE2MTA0OTE@._V1_FMjpg_UX1000_.jpg',
 'ACTIVE');

INSERT IGNORE INTO movie_genre (movie_id, genre_id) VALUES
(1, 1),
(1, 2),
(1, 3);

INSERT IGNORE INTO rooms (id, name, total_seats) VALUES
(1, 'Phòng 1 (Standard)', 20),
(2, 'Phòng 2 (IMAX)', 20);

INSERT IGNORE INTO seats (id, seat_name, room_id) VALUES
(1, 'A1', 1), (2, 'A2', 1), (3, 'A3', 1), (4, 'A4', 1), (5, 'A5', 1),
(6, 'A6', 1), (7, 'A7', 1), (8, 'A8', 1), (9, 'A9', 1), (10, 'A10', 1),
(11, 'B1', 1), (12, 'B2', 1), (13, 'B3', 1), (14, 'B4', 1), (15, 'B5', 1),
(16, 'B6', 1), (17, 'B7', 1), (18, 'B8', 1), (19, 'B9', 1), (20, 'B10', 1),
(21, 'A1', 2), (22, 'A2', 2), (23, 'A3', 2), (24, 'A4', 2), (25, 'A5', 2),
(26, 'A6', 2), (27, 'A7', 2), (28, 'A8', 2), (29, 'A9', 2), (30, 'A10', 2),
(31, 'B1', 2), (32, 'B2', 2), (33, 'B3', 2), (34, 'B4', 2), (35, 'B5', 2),
(36, 'B6', 2), (37, 'B7', 2), (38, 'B8', 2), (39, 'B9', 2), (40, 'B10', 2);

-- Optional demo showtime. Change the date if it has passed.
INSERT IGNORE INTO showtimes (id, movie_id, room_id, start_time, end_time) VALUES
(1, 1, 1, '2026-05-23 19:00:00', '2026-05-23 21:15:00');
