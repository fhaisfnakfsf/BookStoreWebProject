import java.io.IOException;
import java.util.Scanner;

/** Lớp trừu tượng - các menu kế thừa và ghi đè phương thức (đa hình). */
public abstract class Menu {
    protected final Scanner scanner;
    protected final CuaHang cuaHang;
    protected final TaiKhoan nguoiDung;

    protected Menu(Scanner scanner, CuaHang cuaHang, TaiKhoan nguoiDung) {
        if (scanner == null || cuaHang == null || nguoiDung == null)
            throw new IllegalArgumentException("Menu chưa được khởi tạo đủ");
        this.scanner = scanner;
        this.cuaHang = cuaHang;
        this.nguoiDung = nguoiDung;
    }

    protected abstract void hienThi();
    protected abstract int luaChonToiDa();
    protected abstract void xuLy(int luaChon) throws IOException;

    // Template method: gọi đa hình hienThi() và xuLy() theo loại menu thực tế.
    public final void chay() throws IOException {
        while (true) {
            hienThi();
            int chon = KiemTra.nhapSoNguyen(scanner, "Chọn chức năng: ", 0, luaChonToiDa());
            if (chon == 0) {
                System.out.println("Đã quay lại/đăng xuất khỏi menu.");
                return;
            }
            try {
                xuLy(chon);
            } catch (IllegalArgumentException | ArithmeticException ex) {
                System.out.println("[LỖI] " + ex.getMessage());
            }
        }
    }

    protected void luu() throws IOException {
        cuaHang.ghiFile();
        System.out.println("[OK] Đã lưu dữ liệu vào file text.");
    }

    protected void tieuDe(String tenMenu) {
        System.out.printf("%n========== %s ==========%n", tenMenu);
        System.out.println("Đăng nhập: " + nguoiDung);
    }

    protected void xemSanPham() {
        cuaHang.inDanhSachSanPham(cuaHang.layDanhSach());
    }

    protected void timSanPham() {
        System.out.println("1. Mã chính xác   2. Tên gần đúng");
        int chon = KiemTra.nhapSoNguyen(scanner, "Chọn: ", 1, 2);
        if (chon == 1) {
            SanPham sp = cuaHang.timTheoMa(KiemTra.nhapMa(scanner, "Mã sản phẩm: "));
            System.out.println(sp == null ? "Không tìm thấy." : sp);
        } else {
            String ten = KiemTra.nhapChuoi(scanner, "Từ khóa: ", 1, 100);
            cuaHang.inDanhSachSanPham(cuaHang.timTheoTenGanDung(ten));
        }
    }

    protected void xemHoaDon() {
        HoaDon[] ds = cuaHang.layHoaDon();
        if (ds.length == 0) {
            System.out.println("Chưa có hóa đơn.");
            return;
        }
        for (HoaDon hd : ds) {
            System.out.printf("%-10s %-15s %-18s %s%n", hd.getMa(), hd.getNguoiBan(),
                    TienIch.dinhDangNgayGio(hd.getNgayLap()),
                    TienIch.dinhDangTien(hd.tinhTongTien()));
        }
        if (KiemTra.xacNhan(scanner, "Xem chi tiết một hóa đơn?")) {
            String ma = KiemTra.nhapMa(scanner, "Nhập mã hóa đơn: ");
            boolean co = false;
            for (HoaDon hd : ds) {
                if (hd.getMa().equalsIgnoreCase(ma)) {
                    hd.inHoaDon();
                    co = true;
                    break;
                }
            }
            if (!co) System.out.println("Không tìm thấy mã hóa đơn.");
        }
    }
}
