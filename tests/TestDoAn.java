import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/** Test tự động, không dùng JUnit để học sinh dễ chạy. */
public final class TestDoAn {
    private static int soTest = 0;

    private static void check(boolean dieuKien, String thongBao) {
        if (!dieuKien) throw new AssertionError("FAIL: " + thongBao);
        soTest++;
    }

    private static void expectIllegal(Runnable action, String thongBao) {
        try { action.run(); }
        catch (IllegalArgumentException ex) { soTest++; return; }
        throw new AssertionError("FAIL: " + thongBao);
    }

    public static void main(String[] args) throws Exception {
        Path tmp = Files.createTempDirectory("oop-tests-");
        CuaHang ch = KhoiTaoDuLieu.taoCuaHang(tmp);
        check(ch.layDanhSach().length == 3, "3 sản phẩm mặc định");
        check(ch.layTaiKhoan().length == 3, "3 tài khoản mặc định");
        check(ch.dangNhap("admin", "admin123") != null, "đăng nhập đúng");
        check(ch.dangNhap("admin", "sai") == null, "từ chối sai mật khẩu");
        check(ch.dangNhap("banhang", "banhang123").getQuyen() == Quyen.BAN_HANG,
                "đúng quyền bán hàng");
        check(ch.timTheoMa("sp02").getLoai().equals("KHUYEN_MAI"), "tìm không phân biệt hoa/thường");
        check(ch.timTheoMa("SP02").tinhDonGia(2) == 10200, "đa hình giảm 15%");
        check(ch.timTheoMa("SP03").tinhDonGia(9) == 65000, "giá sỉ mua 9 món");
        check(ch.timTheoMa("SP03").tinhDonGia(10) == 58500, "giá sỉ mua 10 món");
        check(ch.timTheoTenGanDung("vở").length == 1, "tìm tên gần đúng Unicode");
        check(!ch.them(new SanPhamThuong("SP01", "Sổ tay", 20000, 10)), "không thêm trùng mã");
        expectIllegal(() -> ch.nhapKho("SP01", -2), "không cho nhập kho âm");

        TaiKhoan admin = ch.dangNhap("admin", "admin123");
        TaiKhoan thuKho = ch.dangNhap("kho", "kho123");
        Map<String,Integer> gio = new LinkedHashMap<>();
        gio.put("SP02", 2);
        gio.put("SP03", 10);
        expectIllegal(() -> ch.banHang(thuKho, gio), "phân quyền không cho kho bán");
        check(ch.timTheoMa("SP02").getTonKho() == 80, "bán sai quyền không thay tồn");

        HoaDon hd = ch.banHang(admin, gio);
        check(hd.tinhTongTien() == (10200L * 2 + 58500L * 10), "tính tiền hóa đơn");
        check(ch.timTheoMa("SP02").getTonKho() == 78, "trừ tồn mã 1");
        check(ch.timTheoMa("SP03").getTonKho() == 50, "trừ tồn mã 2");
        check(ch.tinhDoanhThu() == hd.tinhTongTien(), "thống kê doanh thu");

        Map<String,Integer> loi = new LinkedHashMap<>();
        loi.put("SP01", 1);
        loi.put("SP03", 99);
        expectIllegal(() -> ch.banHang(admin, loi), "không đủ kho: không tạo hóa đơn");
        check(ch.timTheoMa("SP01").getTonKho() == 100, "giao dịch lỗi không trừ mã trước");
        check(ch.layHoaDon().length == 1, "giao dịch lỗi không thêm hóa đơn");

        Map<String,Integer> trungMa = new LinkedHashMap<>();
        trungMa.put("SP01", 60);
        trungMa.put("sp01", 60);
        expectIllegal(() -> ch.banHang(admin, trungMa), "không bỏ lọt mã nhập trùng hoa/thường");
        check(ch.timTheoMa("SP01").getTonKho() == 100, "không trừ kho khi trùng mã quá tồn");
        check(ch.layHoaDon().length == 1, "không tạo hóa đơn khi trùng mã quá tồn");

        ch.ghiFile();
        CuaHang phucHoi = new CuaHang("Thử tải", tmp);
        phucHoi.docFile();
        check(phucHoi.layHoaDon().length == 1, "đọc lại hóa đơn từ TXT");
        check(phucHoi.timTheoMa("SP02").getTonKho() == 78, "đọc lại tồn kho");
        check(phucHoi.dangNhap("admin", "admin123") != null, "đọc lại mật khẩu đã băm");
        check(phucHoi.tinhDoanhThu() == hd.tinhTongTien(), "doanh thu sau khi khởi động lại");
        check(Files.exists(tmp.resolve("sanpham.txt")) && Files.exists(tmp.resolve("taikhoan.txt"))
                && Files.exists(tmp.resolve("hoadon.txt")), "có đủ 3 file TXT");

        String dauVao = "abc\n2.5\n-1\n999\n3\n";
        try (Scanner sc = new Scanner(new ByteArrayInputStream(dauVao.getBytes(StandardCharsets.UTF_8)), "UTF-8")) {
            check(KiemTra.nhapSoNguyen(sc, "Test số nguyên: ", 0, 5) == 3,
                    "nhập sai kiểu và quá khoảng thì yêu cầu nhập lại");
        }
        String tienSai = "1e5\n12.5\n-7\n120000\n";
        try (Scanner sc = new Scanner(tienSai)) {
            check(KiemTra.nhapSoTien(sc, "Test tiền: ", 1, 200000) == 120000,
                    "tiền không được thập phân và ký hiệu khoa học");
        }
        try (Scanner sc = new Scanner("NaN\nInfinity\n2,5\n12.75\n")) {
            check(KiemTra.nhapSoThuc(sc, "Test số thực: ", 0, 20) == 12.75,
                    "số thực loại bỏ NaN, Infinity, dấu phẩy");
        }
        try (Scanner sc = new Scanner("31/02/2026\n29/02/2028\n")) {
            check(KiemTra.nhapNgay(sc, "Test ngày").getDayOfMonth() == 29,
                    "ngày tháng phải hợp lệ, có kiểm tra năm nhuận");
        }
        check(ch.doanhThuTheoNhanVien("admin") == hd.tinhTongTien(),
                "thống kê doanh thu theo nhân viên");
        check(ch.tongSoLuongDaBan("SP03") == 10,
                "thống kê theo mã sản phẩm");
        check(ch.doanhThuTheoNgay(hd.getNgayLap().toLocalDate()) == hd.tinhTongTien(),
                "thống kê theo ngày bán");
        System.out.println("\nTHÀNH CÔNG: " + soTest + " kiểm thử đã vượt qua.");
        System.out.println("Thư mục dữ liệu kiểm thử: " + tmp);
    }
}
