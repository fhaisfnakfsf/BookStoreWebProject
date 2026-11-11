import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Chỉ tạo dữ liệu mẫu ở lần chạy đầu tiên; các lần sau đọc file đã lưu. */
public final class KhoiTaoDuLieu {
    private KhoiTaoDuLieu() { }

    public static CuaHang taoCuaHang(Path thuMuc) throws IOException {
        CuaHang cuaHang = new CuaHang("Cửa hàng bán lẻ OOP", thuMuc);
        Path taiKhoan = thuMuc.resolve("taikhoan.txt");
        Path sanPham = thuMuc.resolve("sanpham.txt");
        Path hoaDon = thuMuc.resolve("hoadon.txt");
        if (Files.exists(taiKhoan) || Files.exists(sanPham) || Files.exists(hoaDon)) {
            cuaHang.docFile();
            System.out.println("[OK] Đã tải dữ liệu từ thư mục " + thuMuc);
        } else {
            cuaHang.themTaiKhoan(new TaiKhoan("admin", "admin123", Quyen.QUAN_LY));
            cuaHang.themTaiKhoan(new TaiKhoan("banhang", "banhang123", Quyen.BAN_HANG));
            cuaHang.themTaiKhoan(new TaiKhoan("kho", "kho123", Quyen.KHO));
            cuaHang.them(new SanPhamThuong("SP01", "Bút bi", 5_000, 100));
            cuaHang.them(new SanPhamKhuyenMai("SP02", "Vở học sinh", 12_000, 80, 15));
            cuaHang.them(new SanPhamGiaSi("SP03", "Giấy A4", 65_000, 60));
            cuaHang.ghiFile();
            System.out.println("[OK] Đã tạo dữ liệu mẫu và lưu file text tại " + thuMuc);
        }
        return cuaHang;
    }
}
