import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Điểm bắt đầu chạy chương trình. */
public final class ChuongTrinh {
    private ChuongTrinh() { }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8.name())) {
            CuaHang cuaHang = KhoiTaoDuLieu.taoCuaHang(Paths.get("data"));
            System.out.println("=== " + cuaHang.getTen() + " ===");
            while (true) {
                System.out.println("\n1. Đăng nhập");
                System.out.println("0. Thoát chương trình");
                int chon = KiemTra.nhapSoNguyen(sc, "Chọn: ", 0, 1);
                if (chon == 0) {
                    System.out.println("Đã thoát. Tạm biệt!");
                    break;
                }
                String ten = KiemTra.nhapTenDangNhap(sc, "Tài khoản: ");
                String matKhau = KiemTra.nhapChuoi(sc, "Mật khẩu: ", 1, 60);
                TaiKhoan dangNhap = cuaHang.dangNhap(ten, matKhau);
                if (dangNhap == null) {
                    System.out.println("[LỖI] Tài khoản hoặc mật khẩu không đúng.");
                    continue;
                }
                Menu menu;
                switch (dangNhap.getQuyen()) {
                    case QUAN_LY: menu = new MenuQuanLy(sc, cuaHang, dangNhap); break;
                    case BAN_HANG: menu = new MenuBanHang(sc, cuaHang, dangNhap); break;
                    case KHO: menu = new MenuKho(sc, cuaHang, dangNhap); break;
                    default: throw new IllegalStateException("Quyền không hỗ trợ");
                }
                menu.chay(); // ĐA HÌNH: biến Menu gọi phương thức lớp con
            }
        } catch (NoSuchElementException ex) {
            System.out.println("\nĐã kết thúc nhập dữ liệu. Chương trình đóng an toàn.");
        } catch (IOException ex) {
            System.err.println("[LỖI FILE] " + ex.getMessage());
            System.err.println("Hãy kiểm tra quyền truy cập thư mục data và nội dung file.");
        }
    }
}
