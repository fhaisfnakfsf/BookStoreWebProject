import java.io.IOException;
import java.util.Scanner;

public final class MenuQuanLy extends Menu {
    public MenuQuanLy(Scanner scanner, CuaHang cuaHang, TaiKhoan nguoiDung) {
        super(scanner, cuaHang, nguoiDung);
        if (nguoiDung.getQuyen() != Quyen.QUAN_LY)
            throw new IllegalArgumentException("Chỉ quản lý mới được vào menu này");
    }

    @Override protected void hienThi() {
        tieuDe("QUẢN LÝ CỬA HÀNG");
        System.out.println("1. Thống kê doanh thu và tồn kho");
        System.out.println("2. Xem danh sách tài khoản");
        System.out.println("3. Tạo tài khoản nhân viên");
        System.out.println("4. Đặt lại mật khẩu tài khoản");
        System.out.println("5. Vào chức năng kho");
        System.out.println("6. Vào chức năng bán hàng");
        System.out.println("7. Xem hóa đơn");
        System.out.println("8. Thống kê theo ngày / nhân viên / sản phẩm");
        System.out.println("0. Đăng xuất");
    }

    @Override protected int luaChonToiDa() { return 8; }

    @Override protected void xuLy(int chon) throws IOException {
        switch (chon) {
            case 1: cuaHang.hienThiThongKe(); break;
            case 2: xemTaiKhoan(); break;
            case 3: taoTaiKhoan(); break;
            case 4: datLaiMatKhau(); break;
            case 5: new MenuKho(scanner, cuaHang, nguoiDung).chay(); break;
            case 6: new MenuBanHang(scanner, cuaHang, nguoiDung).chay(); break;
            case 7: xemHoaDon(); break;
            case 8: thongKeChiTiet(); break;
            default: throw new IllegalArgumentException("Chức năng chưa có");
        }
    }

    private void thongKeChiTiet() {
        System.out.println("1. Doanh thu theo ngày");
        System.out.println("2. Doanh thu theo tên nhân viên");
        System.out.println("3. Số lượng đã bán theo mã sản phẩm");
        int chon = KiemTra.nhapSoNguyen(scanner, "Chọn: ", 1, 3);
        if (chon == 1) {
            java.time.LocalDate ngay = KiemTra.nhapNgay(scanner, "Nhập ngày cần xem");
            System.out.println("Doanh thu: " + TienIch.dinhDangTien(cuaHang.doanhThuTheoNgay(ngay)));
        } else if (chon == 2) {
            String ten = KiemTra.nhapTenDangNhap(scanner, "Tên tài khoản nhân viên: ");
            System.out.println("Doanh thu: " + TienIch.dinhDangTien(cuaHang.doanhThuTheoNhanVien(ten)));
        } else {
            String ma = KiemTra.nhapMa(scanner, "Mã sản phẩm: ");
            System.out.println("Tổng đã bán: " + cuaHang.tongSoLuongDaBan(ma));
        }
    }

    private void xemTaiKhoan() {
        for (TaiKhoan tk : cuaHang.layTaiKhoan()) {
            System.out.println("- " + tk);
        }
    }

    private void taoTaiKhoan() throws IOException {
        String ten = KiemTra.nhapTenDangNhap(scanner, "Tên đăng nhập mới: ");
        if (cuaHang.timTaiKhoan(ten) != null) {
            System.out.println("Tên tài khoản đã tồn tại.");
            return;
        }
        String matKhau = KiemTra.nhapMatKhau(scanner, "Mật khẩu (ít nhất 6 ký tự): ");
        System.out.println("1. Quản lý  2. Nhân viên bán hàng  3. Thủ kho");
        int quyen = KiemTra.nhapSoNguyen(scanner, "Chọn quyền: ", 1, 3);
        Quyen q = quyen == 1 ? Quyen.QUAN_LY : quyen == 2 ? Quyen.BAN_HANG : Quyen.KHO;
        if (cuaHang.themTaiKhoan(new TaiKhoan(ten, matKhau, q))) {
            System.out.println("Đã tạo tài khoản.");
            luu();
        }
    }

    private void datLaiMatKhau() throws IOException {
        String ten = KiemTra.nhapTenDangNhap(scanner, "Tên tài khoản cần đổi mật khẩu: ");
        TaiKhoan tk = cuaHang.timTaiKhoan(ten);
        if (tk == null) {
            System.out.println("Không tìm thấy tài khoản.");
            return;
        }
        String moi = KiemTra.nhapMatKhau(scanner, "Mật khẩu mới: ");
        if (KiemTra.xacNhan(scanner, "Xác nhận đổi mật khẩu cho " + ten + "?")) {
            tk.doiMatKhau(moi);
            luu();
            System.out.println("Đã đổi mật khẩu.");
        }
    }
}
