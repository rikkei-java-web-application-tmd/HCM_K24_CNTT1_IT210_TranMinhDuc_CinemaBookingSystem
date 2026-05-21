# Kịch bản kiểm thử demo Cinema Booking

Dữ liệu mẫu nằm trong file `database/seed-data.sql`. Mặc định `DataSeeder` Java đã tắt bằng `app.seed.enabled=false`.

Cách chuẩn bị dữ liệu:

1. Chạy app một lần để Hibernate tự tạo database/table trong MySQL.
2. Dừng app nếu muốn.
3. Chạy file `database/seed-data.sql` trong MySQL Workbench/phpMyAdmin/terminal.
4. Chạy lại app để demo.

Lệnh terminal tham khảo:

```bash
mysql -u root -p < database/seed-data.sql
```

Tài khoản mẫu sau khi chạy SQL:

| Vai trò | Username | Password |
| --- | --- | --- |
| Admin | `admin` | `123456` |
| Customer | `customer` | `123456` |

Ghi chú dữ liệu thời gian: ví dụ dưới đây dùng suất chiếu `23/05/2026 19:00`, vì ngày hiện tại khi chuẩn bị bài là `21/05/2026`. Nếu ngày demo đã qua mốc này, hãy chọn một ngày tương lai cách thời điểm demo ít nhất 2 ngày để có thể test hủy vé trước 24 tiếng.

## Luồng demo chính

### Bước 1: Khởi động ứng dụng

1. Chạy project Spring Boot.
2. Chờ console hiện thông báo ứng dụng đã khởi động.
3. Trình duyệt mặc định tự mở vào `http://localhost:8080`.

Kết quả mong đợi:

- Trang chủ hiển thị danh sách lịch chiếu.
- Nếu chưa có suất chiếu hợp lệ, trang báo `Chưa có suất chiếu còn hiệu lực`.

### Bước 2: Đăng nhập Admin

1. Bấm `Đăng nhập`.
2. Nhập:
   - Tên đăng nhập: `admin`
   - Mật khẩu: `123456`
3. Bấm `Đăng nhập`.

Kết quả mong đợi:

- Đăng nhập thành công.
- Hệ thống tự chuyển về `/admin/movies`.
- Thanh điều hướng có `Quản lý phim`, `Quản lý suất chiếu`, tài khoản `admin`, nút `Đăng xuất`.

### Bước 3: Tạo phim mới

1. Ở trang `Quản lý phim`, bấm `Thêm phim`.
2. Nhập dữ liệu:
   - Tên phim: `Demo CORE Cinema`
   - Đạo diễn: `Tran Minh Duc`
   - Thời lượng: `120`
   - Trạng thái: `Đang chiếu`
   - Poster URL: có thể để trống hoặc nhập một URL bất kỳ.
   - Mô tả: `Phim dùng để demo đặt vé.`
   - Thể loại: chọn ít nhất 1 thể loại.
3. Bấm `Lưu`.

Kết quả mong đợi:

- Hệ thống chuyển về `/admin/movies`.
- Danh sách phim có phim `Demo CORE Cinema`.
- Có thông báo lưu phim thành công.

### Bước 4: Tạo suất chiếu hợp lệ

1. Bấm `Quản lý suất chiếu`.
2. Bấm `Xếp lịch mới`.
3. Nhập dữ liệu:
   - Phim: `Demo CORE Cinema - 120 phút`
   - Phòng: `Phòng 1 (Standard)`
   - Giờ bắt đầu: `23/05/2026 19:00`
4. Bấm `Lưu lịch chiếu`.

Kết quả mong đợi:

- Hệ thống chuyển về `/admin/showtimes`.
- Danh sách có suất chiếu mới.
- Giờ kết thúc khoảng `23/05/2026 21:15` vì hệ thống cộng `120 phút` phim và `15 phút` dọn phòng.

### Bước 5: Test CORE-05 - chặn xung đột giờ chiếu

1. Bấm `Xếp lịch mới`.
2. Nhập dữ liệu:
   - Phim: `Demo CORE Cinema - 120 phút`
   - Phòng: `Phòng 1 (Standard)`
   - Giờ bắt đầu: `23/05/2026 20:00`
3. Bấm `Lưu lịch chiếu`.

Kết quả mong đợi:

- Hệ thống không tạo suất chiếu mới.
- Trang vẫn ở form xếp lịch.
- Có thông báo lỗi: `Phòng này đã có lịch chiếu chồng lên khoảng thời gian bạn chọn.`

### Bước 6: Đăng xuất Admin

1. Bấm `Đăng xuất`.

Kết quả mong đợi:

- Hệ thống quay về trang chủ `/`.
- Thanh điều hướng hiện `Đăng nhập`, `Đăng ký`.

### Bước 7: Đăng nhập Customer

1. Bấm `Đăng nhập`.
2. Nhập:
   - Tên đăng nhập: `customer`
   - Mật khẩu: `123456`
3. Bấm `Đăng nhập`.

Kết quả mong đợi:

- Đăng nhập thành công.
- Hệ thống chuyển về trang chủ `/`.
- Thanh điều hướng có `Lịch sử vé`, tài khoản `customer`, nút `Đăng xuất`.

### Bước 8: Chọn suất chiếu và ghế

1. Ở trang chủ, tìm phim `Demo CORE Cinema`.
2. Bấm `Xem ghế / Đặt vé`.
3. Chọn ghế `A1` và `A2`.
4. Bấm `Thanh toán`.

Kết quả mong đợi:

- Hệ thống tạo booking và ticket trong cùng transaction.
- Sau khi đặt vé thành công, hệ thống tự chuyển sang `/history`.
- Có thông báo `Đặt vé thành công. Mã hóa đơn: #...`.
- Lịch sử vé hiển thị phim, suất chiếu, phòng, ghế `A1, A2`, tổng tiền `150,000 VND`, trạng thái `Đã thanh toán`.

### Bước 9: Test CORE-06 - chống mua trùng ghế

1. Bấm `Trang chủ`.
2. Mở lại suất chiếu `Demo CORE Cinema`.
3. Quan sát ghế `A1`, `A2`.

Kết quả mong đợi:

- Ghế `A1`, `A2` hiển thị trạng thái `Đã bán`.
- Checkbox của các ghế này bị khóa, không thể chọn lại.
- Nếu có thao tác đặt trùng bằng request khác, service sẽ báo ghế vừa được người khác mua và rollback giao dịch.

### Bước 10: Test CORE-09 - hủy vé

1. Bấm `Lịch sử vé`.
2. Ở hóa đơn vừa đặt, bấm `Hủy vé`.
3. Xác nhận hộp thoại hủy.

Kết quả mong đợi:

- Hệ thống chuyển lại `/history`.
- Có thông báo hủy vé thành công.
- Hóa đơn chuyển trạng thái `Đã hủy`.
- Nút `Hủy vé` biến mất.

### Bước 11: Kiểm tra ghế được giải phóng sau khi hủy

1. Bấm `Trang chủ`.
2. Mở lại suất chiếu `Demo CORE Cinema`.
3. Kiểm tra ghế `A1`, `A2`.

Kết quả mong đợi:

- Ghế `A1`, `A2` trở về trạng thái `Còn trống`.
- Customer có thể chọn lại các ghế này cho booking mới.

## Luồng phụ nên chuẩn bị

### Đăng ký khách hàng mới

1. Đăng xuất.
2. Bấm `Đăng ký`.
3. Nhập:
   - Tên đăng nhập: `demo_customer`
   - Mật khẩu: `123456`
   - Họ tên: `Khach Demo`
   - Số điện thoại: `0912345678`
   - Email: `demo_customer@example.com`
4. Bấm `Tạo tài khoản`.

Kết quả mong đợi:

- Hệ thống chuyển về trang đăng nhập.
- Có thông báo đăng ký thành công.

### Cập nhật hồ sơ

1. Đăng nhập bằng `customer`.
2. Bấm tên tài khoản `customer`.
3. Sửa họ tên hoặc số điện thoại.
4. Bấm `Lưu hồ sơ`.

Kết quả mong đợi:

- Hệ thống vẫn ở `/profile`.
- Có thông báo cập nhật hồ sơ thành công.
