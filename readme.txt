======================================================
 ĐỒ ÁN OOP: HỆ THỐNG QUẢN LÝ CỬA HÀNG SÁCH (BOOKSTORE)
======================================================

I. SƠ ĐỒ CẤU TRÚC THƯ MỤC (PROJECT STRUCTURE)
------------------------------------------------------
Dự án được chia thành 2 phần chính: Giao diện (FRONTEND) và Xử lý logic (BACKEND_OOP).

BookStoreWebProject/
├── BACKEND_OOP/               # Chứa toàn bộ code xử lý logic (Class, File I/O)
│   └── database/              # Thư mục lưu trữ Cơ sở dữ liệu (các file .txt)
│
└── FRONTEND/                  # Chứa toàn bộ giao diện web (HTML/CSS)
    ├── index.html             # Trang giới thiệu (Landing page)
    ├── index.css
    ├── login.html             # Trang đăng nhập
    ├── login.css
    ├── register.html          # Trang đăng ký
    ├── register.css
    │
    ├── admin/                 # Phân hệ GIAO DIỆN QUẢN TRỊ VIÊN
    │   ├── main.html          # [KHUNG] Chứa Sidebar menu và Iframe (Trưởng nhóm giữ)
    │   ├── main.css
    │   ├── dashboard.html     # [RUỘT] Thống kê tổng quan
    │   ├── dashboard.css
    │   ├── file-handler.html  # [RUỘT] Thao tác Đọc/Ghi dữ liệu ra File Text
    │   ├── file-handler.css
    │   ├── manage-books.html  # [RUỘT] Quản lý thêm/sửa/xóa sách
    │   ├── manage-books.css
    │   ├── manage-orders.html # [RUỘT] Duyệt đơn hàng
    │   ├── manage-orders.css
    │   ├── manage-users.html  # [RUỘT] Quản lý tài khoản
    │   └── manage-users.css
    │
    └── client/                # Phân hệ GIAO DIỆN KHÁCH HÀNG
        ├── main.html          # [KHUNG] Chứa Sidebar menu và Iframe (Trưởng nhóm giữ)
        ├── main.css
        ├── dashboard.html     # [RUỘT] Trang chủ / Sách nổi bật
        ├── dashboard.css
        ├── textbooks.html     # [RUỘT] Danh mục: Sách giáo khoa
        ├── textbooks.css
        ├── novels.html        # [RUỘT] Danh mục: Tiểu thuyết
        ├── novels.css
        ├── comics.html        # [RUỘT] Danh mục: Truyện tranh
        ├── comics.css
        ├── cart.html          # [RUỘT] Giỏ hàng & Thanh toán
        ├── cart.css
        ├── orders.html        # [RUỘT] Lịch sử đơn hàng của khách
        ├── orders.css
        ├── profile.html       # [RUỘT] Thông tin tài khoản
        └── profile.css


II. CƠ SỞ DỮ LIỆU (DATABASE - FILE I/O)
------------------------------------------------------
Do yêu cầu của môn học OOP, dự án không dùng SQL mà sử dụng Đọc/Ghi File Text để lưu trữ dữ liệu.
Tất cả file text được lưu trong thư mục: `BACKEND_OOP/database/`

1. books.txt  : Lưu danh sách sách trong kho. (Mã sách, Tên sách, Thể loại, Tác giả, Giá, Số lượng).
2. users.txt  : Lưu thông tin tài khoản đăng nhập. (ID, Họ tên, Email, Mật khẩu, Quyền [Admin/Client]).
3. orders.txt : Lưu lịch sử hóa đơn. (Mã ĐH, Tên khách, Ngày đặt, Tổng tiền, Trạng thái).


III. QUY TẮC LÀM VIỆC NHÓM (TEAM RULES)
------------------------------------------------------
Để tránh "đụng code" (Merge Conflict) và làm hỏng giao diện của nhau, toàn bộ 4 thành viên phải tuân thủ tuyệt đối 3 quy tắc sau:

 QUY TẮC 1: VÙNG CẤM BẤT KHẢ XÂM PHẠM
- Hai file `admin/main.html` và `client/main.html` là móng nhà của dự án. 
- CHỈ DUY NHẤT Trưởng nhóm được quyền mở và chỉnh sửa 2 file này. 
- Các thành viên khác nếu cần thêm menu hoặc đổi tên menu phải báo cáo để Trưởng nhóm sửa.

 QUY TẮC 2: CODE ĐÚNG NƠI QUY ĐỊNH (DOMAIN-DRIVEN)
- Ai được phân công làm trang nào (file ruột) thì CHỈ mở file HTML/CSS của trang đó lên để code.
- Tuyệt đối KHÔNG viết code sang file của người khác. Tránh tình trạng người này viết đè lên công sức của người kia.

 QUY TẮC 3: BACKUP TRƯỚC KHI RÁP CODE
- Khi hoàn thành phần việc, tự nén thư mục của mình lại (.zip) gửi cho Trưởng nhóm.
- Trưởng nhóm chịu trách nhiệm copy code của từng người ráp vào project tổng. Đảm bảo chạy thử không lỗi mới được chốt.