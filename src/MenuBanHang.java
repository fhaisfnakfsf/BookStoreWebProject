import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public final class MenuBanHang extends Menu {
    public MenuBanHang(Scanner scanner, CuaHang cuaHang, TaiKhoan nguoiDung) {
        super(scanner, cuaHang, nguoiDung);
        if (nguoiDung.getQuyen() != Quyen.BAN_HANG && nguoiDung.getQuyen() != Quyen.QUAN_LY)
            throw new IllegalArgumentException("Không được truy cập menu bán hàng");
    }

    @Override protected void hienThi() {
        tieuDe("BÁN HÀNG");
        System.out.println("1. Xem sản phẩm");
        System.out.println("2. Tìm kiếm sản phẩm");
        System.out.println("3. Tạo hóa đơn bán hàng");
        System.out.println("4. Xem lịch sử hóa đơn");
        System.out.println("0. Quay lại/đăng xuất");
    }

    @Override protected int luaChonToiDa() { return 4; }

    @Override protected void xuLy(int chon) throws IOException {
        switch (chon) {
            case 1: xemSanPham(); break;
            case 2: timSanPham(); break;
            case 3: taoHoaDon(); break;
            case 4: xemHoaDon(); break;
            default: throw new IllegalArgumentException("Chức năng chưa có");
        }
    }

    private void taoHoaDon() throws IOException {
        if (cuaHang.layDanhSach().length == 0) {
            System.out.println("Kho chưa có hàng để bán.");
            return;
        }
        xemSanPham();
        int n = KiemTra.nhapSoNguyen(scanner, "Số dòng sản phẩm muốn mua (1-30): ", 1, 30);
        Map<String, Integer> gio = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            System.out.printf("Dòng %d/%d%n", i + 1, n);
            String ma = KiemTra.nhapMa(scanner, "Mã: ");
            int soLuong = KiemTra.nhapSoNguyen(scanner, "Số lượng: ", 1, 1_000_000);
            gio.merge(ma, soLuong, Math::addExact);
        }
        if (!KiemTra.xacNhan(scanner, "Xác nhận thanh toán?")) {
            System.out.println("Đã hủy hóa đơn. Tồn kho giữ nguyên.");
            return;
        }
        HoaDon hd = cuaHang.banHang(nguoiDung, gio);
        hd.inHoaDon();
        luu();
    }
}
