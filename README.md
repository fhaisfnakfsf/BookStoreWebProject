# ĐỒ ÁN LẬP TRÌNH HƯỚNG ĐỐI TƯỢNG – PHẦN NHÓM TRƯỞNG

**Ngôn ngữ:** Java 17+ (chỉ thư viện chuẩn, không cần Maven/MySQL).

Đây là **bộ mã chạy độc lập để thực hành và ghép nhóm** cho phần nhóm trưởng theo danh sách lớp được yêu cầu: `Quyen`, `TaiKhoan`, `Menu`, `MenuQuanLy`, `MenuBanHang`, `MenuKho`, `CuaHang`, `KhoiTaoDuLieu`, `TienIch`, `ChuongTrinh`; bổ sung `KiemTra`, 3 interface và các lớp nghiệp vụ **mẫu** để biên dịch/chạy thử ngay cả khi chưa có mã của các thành viên khác.

> **Quan trọng:** Sơ đồ UML do nhóm cung cấp là ảnh rất nhỏ, không phải mã Mermaid gốc. Vì vậy tên và phương thức của *các lớp sản phẩm/hóa đơn hỗ trợ* trong bản demo có thể khác sơ đồ UML hiện có. Khi ghép nhóm, cần đối chiếu với code thật của các thành viên, thay lớp hỗ trợ/method adapter tương ứng; không nên chép đè các lớp nghiệp vụ đang được người khác phụ trách.

## 1. Chạy chương trình

- Cài **JDK 17 trở lên** (không chỉ JRE), kiểm tra `java -version` và `javac -version`.
- Trên **Windows:** giải nén ZIP, nhấp đúp `run.bat` tại thư mục gốc (hoặc chạy từ terminal), hoặc mở thư mục trong VS Code rồi chạy `ChuongTrinh.main()`.
- Trên **Linux/macOS:** chạy `bash run.sh`.
- Nếu chạy bằng lệnh ở thư mục gốc:

```bash
javac -encoding UTF-8 -d out src/*.java
java -Dfile.encoding=UTF-8 -cp out ChuongTrinh
```

Trên Windows CMD, cách biên dịch tương đương là:

```bat
javac -encoding UTF-8 -d out src\*.java
java -Dfile.encoding=UTF-8 -cp out ChuongTrinh
```

Lần đầu chương trình tạo dữ liệu mẫu trong `data/` nếu chưa có cả 3 file dữ liệu. Các lần sau chương trình tự tải dữ liệu và **không tạo lại mặc định**.

### Tài khoản DEMO

| Tài khoản | Mật khẩu | Quyền |
|---|---|---|
| `admin` | `admin123` | Quản lý |
| `banhang` | `banhang123` | Bán hàng |
| `kho` | `kho123` | Thủ kho |

**Lưu ý:** Đây chỉ là tài khoản thử nghiệm. Mật khẩu lưu dưới dạng băm SHA-256 theo tên đăng nhập để tránh lưu trực tiếp; **không phù hợp cho hệ thống thực tế** (nên dùng Argon2/PBKDF2 với salt ngẫu nhiên).

## 2. Phân chia lớp

### Chính xác các lớp nhóm trưởng được giao

| Thành phần | Nhiệm vụ |
|---|---|
| `Quyen` | `enum` 3 loại quyền: `QUAN_LY`, `BAN_HANG`, `KHO` |
| `TaiKhoan` | Thông tin đăng nhập, băm và kiểm tra mật khẩu, quyền |
| `Menu` | **abstract**: khung menu chung + phương thức trừu tượng |
| `MenuQuanLy` | Thống kê, tài khoản, chuyển sang menu kho/bán hàng |
| `MenuBanHang` | Tra cứu sản phẩm, bán hàng, tạo hóa đơn |
| `MenuKho` | CRUD sản phẩm, thêm k phần tử, nhập kho, tìm kiếm |
| `CuaHang` | Bộ điều phối, mảng các sản phẩm/tài khoản/hóa đơn, triển khai 3 interface |
| `KhoiTaoDuLieu` | Đọc dữ liệu cũ hoặc tạo dữ liệu demo khi chạy lần đầu |
| `TienIch` | Các phương thức `static`: định dạng tiền, mã hóa chuỗi TXT, sinh mã |
| `ChuongTrinh` | `main`: đăng nhập, phân quyền, chuyển menu |
| `KiemTra` | **Lớp thêm theo yêu cầu:** nhập đúng kiểu, giới hạn, ngày tháng, xác nhận |

### 3 interface

1. **`IQuanLy<T>`**: thêm, sửa theo mã, xóa theo mã, tìm theo mã, lấy danh sách.
2. **`ILuuFile`**: `docFile()` và `ghiFile()` trên file `.txt` UTF-8.
3. **`IThongKe`**: `tinhDoanhThu()`, `tongSoLuongTon()`, `hienThiThongKe()`.

`CuaHang implements IQuanLy<SanPham>, ILuuFile, IThongKe` nên **có khai báo và sử dụng thực tế**, không phải tạo interface để lấy điểm mà không dùng.

### Các lớp hỗ trợ chạy thử (thành viên khác có thể thay thế)

- `SanPham` (**abstract**), 3 lớp kế thừa `SanPhamThuong`, `SanPhamKhuyenMai`, `SanPhamGiaSi` (đa hình ở `tinhDonGia(int)`).
- `HoaDon`, `ChiTietHoaDon` (tạo hóa đơn có nhiều mặt hàng, lưu đơn giá tại thời điểm bán).

Bản demo có **16 class, 3 interface, 1 enum**. Khi ghép với code của cả nhóm, số lớp và quan hệ thực tế phải kiểm lại để khớp UML chung.

## 3. Lớp `KiemTra`: chống nhập sai kiểu dữ liệu

Tất cả thao tác nhập console đi qua `Scanner.nextLine()` và `KiemTra` để tránh lỗi `nextInt()`/`nextLine()` bị trôi dòng:

| Phương thức | Kiểm tra |
|---|---|
| `nhapSoNguyen` | Chỉ `int`, có min-max, nhập lại nếu sai chữ, `2.5`, số quá giới hạn |
| `nhapSoTien` | Chỉ tiền nguyên VND, không âm/không thập phân/không số mũ |
| `nhapSoThuc` | `double` hữu hạn (không `NaN`/`Infinity`), đúng giới hạn |
| `nhapNgay` | Dạng `dd/MM/yyyy`, kiểm tra ngày thật và năm nhuận |
| `nhapMa` | Chữ không dấu, số, `_`, `-`, dài 2-20 |
| `nhapTenDangNhap` | Tên hợp lệ 3-24 ký tự |
| `nhapMatKhau` | Dài 6-60 ký tự |
| `nhapChuoi` | Không để trống khi đặt min > 0; giới hạn độ dài |
| `xacNhan` | Chỉ chấp nhận `c` hoặc `k` (có/không) |

**Ví dụ:** Nhập số lượng `abc`, `1.5`, `-3`, hoặc `9999999` đều được yêu cầu nhập lại thay vì sập chương trình.

## 4. Nghiệp vụ được hỗ trợ

- **Quản lý:** tạo tài khoản và gán quyền, đặt lại mật khẩu, thống kê doanh thu chung, theo ngày, theo nhân viên; thống kê số lượng bán theo mã; truy cập cả kho và bán hàng.
- **Kho:** nhập một hay nhiều sản phẩm, sửa, xóa (có xác nhận), nhập thêm tồn, tìm chính xác theo mã/gần đúng theo tên.
- **Bán hàng:** tạo hóa đơn gồm nhiều mặt hàng, áp dụng đa hình cho giá, kiểm tra toàn bộ số lượng trước khi trừ kho, lưu hóa đơn và xem lại sau khi khởi động.
- **Phân quyền:** thủ kho không được truy cập chức năng bán hàng, nhân viên bán hàng không được truy cập quản lý.
- **Mảng đối tượng:** `SanPham[]`, `TaiKhoan[]`, `HoaDon[]` và `ChiTietHoaDon[]` sử dụng `Arrays.copyOf(...)` để nới mảng theo sức chứa; không dùng `ArrayList` để lưu dữ liệu **chính**.
- **Lưu file text:** `data/sanpham.txt`, `data/taikhoan.txt`, `data/hoadon.txt`; định dạng mỗi dòng phân tách bằng `|`, chữ tự do được Base64 hóa để tránh lỗi ký tự phân cách và giữ Unicode. Dù có mã hóa trường, file vẫn là **văn bản TXT**, không phải file nhị phân hay cơ sở dữ liệu.

## 5. Chạy kiểm thử

Chạy `tests/run_tests.bat` trên Windows hoặc `bash tests/run_tests.sh` trên macOS/Linux. Chương trình chạy **36 kiểm thử** về tài khoản, phân quyền, đa hình tính giá, bán hàng an toàn, TXT đọc/ghi, thống kê và chống sai kiểu nhập.

## 6. Khi ghép đồ án với các thành viên khác

1. **Trước tiên thống nhất tên lớp, constructor, getter và phương thức** của `SanPham`, `HoaDon`, `ChiTietHoaDon` theo UML chính thức. Các lớp này **đang là demo**; không tự ý xóa mã của nhóm.
2. Cập nhật các chỗ tham chiếu trong `CuaHang`, `MenuKho`, `MenuBanHang`, `KhoiTaoDuLieu` để dùng lớp nghiệp vụ thật của nhóm. Giữ 3 interface và trách nhiệm từng menu.
3. Nếu nhóm đã có class danh sách trung tâm (ví dụ `DanhSachSanPham`), chuyển trách nhiệm xử lý mảng sang đó; `CuaHang` chỉ gọi qua đối tượng danh sách.
4. Sau khi ghép, biên dịch lại **cả project** và kiểm thử thêm/sửa/xóa, phân quyền, dữ liệu vẫn giữ khi khởi động lại.
5. Sửa tài khoản demo, dữ liệu demo, mô tả cửa hàng, loại sản phẩm và giao diện trước khi nộp.

## 7. Gợi ý giải thích khi giáo viên hỏi

- **Tại sao dùng abstract `Menu`?** Để chia sẻ vòng lặp `chay()` và buộc menu con cài `hienThi()`, `xuLy()`; mỗi menu có hành vi khác nhau khi gọi cùng phương thức (**đa hình**).
- **Đa hình sản phẩm ở đâu?** `SanPham sp` có thể trỏ tới 3 loại, `sp.tinhDonGia(soLuong)` sẽ chạy cách tính giá của lớp con thực tế.
- **Interface khác abstract thế nào?** Interface định nghĩa khả năng phải làm (`IQuanLy`, `ILuuFile`, `IThongKe`), còn `Menu` là lớp cơ sở có trạng thái chung và logic chung.
- **Static ở đâu?** Các hàm tiện ích `TienIch` / `KiemTra`, hằng `SO_LUONG_TOI_THIEU` và `PHAN_TRAM_GIAM` trong `SanPhamGiaSi`.
- **Tại sao dùng `long` thay `double` cho giá tiền?** Giá bán tính theo VND nguyên để tránh sai số dấu phẩy động.
- **Nếu bán vượt tồn kho?** `CuaHang.banHang` kiểm tra tất cả sản phẩm trước khi tạo giao dịch; nếu sai thì không trừ kho và không thêm hóa đơn.

> Đây là phiên bản demo học tập, chưa xử lý đồng thời nhiều máy/nhân viên, tính toàn vẹn ghi đồng thời của nhiều file, hay mật khẩu theo chuẩn production.
