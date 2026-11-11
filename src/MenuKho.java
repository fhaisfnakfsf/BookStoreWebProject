import java.io.IOException;
import java.util.Scanner;

public final class MenuKho extends Menu {
    public MenuKho(Scanner scanner, CuaHang cuaHang, TaiKhoan nguoiDung) {
        super(scanner, cuaHang, nguoiDung);
        if (nguoiDung.getQuyen() != Quyen.KHO && nguoiDung.getQuyen() != Quyen.QUAN_LY)
            throw new IllegalArgumentException("Không được truy cập menu kho");
    }

    @Override protected void hienThi() {
        tieuDe("QUẢN LÝ KHO");
        System.out.println("1. Xem danh sách sản phẩm");
        System.out.println("2. Thêm sản phẩm");
        System.out.println("3. Nhập thêm số lượng tồn");
        System.out.println("4. Sửa thông tin sản phẩm theo mã");
        System.out.println("5. Xóa sản phẩm theo mã");
        System.out.println("6. Tìm sản phẩm");
        System.out.println("7. Thêm nhiều sản phẩm (k phần tử)");
        System.out.println("0. Quay lại/đăng xuất");
    }

    @Override protected int luaChonToiDa() { return 7; }

    @Override protected void xuLy(int chon) throws IOException {
        switch (chon) {
            case 1: xemSanPham(); break;
            case 2: themSanPham(); break;
            case 3: nhapTon(); break;
            case 4: suaSanPham(); break;
            case 5: xoaSanPham(); break;
            case 6: timSanPham(); break;
            case 7: themNhieuSanPham(); break;
            default: throw new IllegalArgumentException("Chức năng chưa có");
        }
    }

    private SanPham nhapSanPham(String ma, int ton) {
        String ten = KiemTra.nhapChuoi(scanner, "Tên sản phẩm: ", 2, 100);
        long gia = KiemTra.nhapSoTien(scanner, "Giá bán gốc (VND): ", 1, 1_000_000_000);
        System.out.println("Loại: 1. Thường | 2. Khuyến mãi | 3. Giá sỉ (>=10 giảm 10%)");
        int loai = KiemTra.nhapSoNguyen(scanner, "Chọn loại: ", 1, 3);
        switch (loai) {
            case 1: return new SanPhamThuong(ma, ten, gia, ton);
            case 2:
                int giam = KiemTra.nhapSoNguyen(scanner, "Phần trăm giảm (1-70): ", 1, 70);
                return new SanPhamKhuyenMai(ma, ten, gia, ton, giam);
            default: return new SanPhamGiaSi(ma, ten, gia, ton);
        }
    }

    private void themSanPham() throws IOException {
        String ma = KiemTra.nhapMa(scanner, "Mã sản phẩm mới: ");
        if (cuaHang.timTheoMa(ma) != null) {
            System.out.println("Mã đã tồn tại, không thể thêm.");
            return;
        }
        int ton = KiemTra.nhapSoNguyen(scanner, "Số lượng nhập kho ban đầu: ", 0, 1_000_000);
        SanPham sp = nhapSanPham(ma, ton);
        if (cuaHang.them(sp)) {
            System.out.println("Đã thêm: " + sp);
            luu();
        }
    }

    private void themNhieuSanPham() throws IOException {
        int k = KiemTra.nhapSoNguyen(scanner, "Số sản phẩm cần thêm (1-30): ", 1, 30);
        for (int i = 0; i < k; i++) {
            System.out.printf("\n--- Thêm sản phẩm %d/%d ---%n", i + 1, k);
            themSanPham();
        }
    }

    private void nhapTon() throws IOException {
        String ma = KiemTra.nhapMa(scanner, "Mã cần nhập thêm: ");
        int sl = KiemTra.nhapSoNguyen(scanner, "Số lượng cần nhập thêm: ", 1, 1_000_000);
        cuaHang.nhapKho(ma, sl);
        System.out.println("Nhập kho thành công.");
        luu();
    }

    private void suaSanPham() throws IOException {
        String ma = KiemTra.nhapMa(scanner, "Mã cần sửa: ");
        SanPham cu = cuaHang.timTheoMa(ma);
        if (cu == null) {
            System.out.println("Không tìm thấy sản phẩm.");
            return;
        }
        System.out.println("Thông tin cũ: " + cu);
        System.out.println("Nhập thông tin mới (mã và tồn kho giữ nguyên):");
        SanPham moi = nhapSanPham(ma, cu.getTonKho());
        if (cuaHang.sua(ma, moi)) {
            System.out.println("Đã cập nhật: " + moi);
            luu();
        }
    }

    private void xoaSanPham() throws IOException {
        String ma = KiemTra.nhapMa(scanner, "Mã cần xóa: ");
        SanPham sp = cuaHang.timTheoMa(ma);
        if (sp == null) {
            System.out.println("Không tìm thấy sản phẩm.");
            return;
        }
        System.out.println(sp);
        if (KiemTra.xacNhan(scanner, "Bạn chắc chắn muốn xóa sản phẩm này?")) {
            if (cuaHang.xoa(ma)) {
                System.out.println("Đã xóa sản phẩm. Hóa đơn cũ vẫn được lưu.");
                luu();
            }
        }
    }
}
