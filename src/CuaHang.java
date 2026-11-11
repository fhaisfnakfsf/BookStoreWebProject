import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Trung tâm hệ thống: quản lý MẢNG đối tượng và triển khai đúng 3 interface. */
public final class CuaHang implements IQuanLy<SanPham>, ILuuFile, IThongKe {
    private final String ten;
    private final Path thuMucDuLieu;
    private SanPham[] sanPhams = new SanPham[8];
    private int soSanPham = 0;
    private TaiKhoan[] taiKhoans = new TaiKhoan[4];
    private int soTaiKhoan = 0;
    private HoaDon[] hoaDons = new HoaDon[8];
    private int soHoaDon = 0;

    public CuaHang(String ten, Path thuMucDuLieu) {
        if (ten == null || ten.isBlank() || thuMucDuLieu == null)
            throw new IllegalArgumentException("Thiếu thông tin cửa hàng");
        this.ten = ten;
        this.thuMucDuLieu = thuMucDuLieu;
    }

    public String getTen() { return ten; }
    public Path getThuMucDuLieu() { return thuMucDuLieu; }

    // ---- Interface IQuanLy<SanPham>: CRUD trên mảng ----
    @Override public boolean them(SanPham sp) {
        if (sp == null || timTheoMa(sp.getMa()) != null) return false;
        if (soSanPham == sanPhams.length) sanPhams = Arrays.copyOf(sanPhams, sanPhams.length * 2);
        sanPhams[soSanPham++] = sp;
        return true;
    }

    @Override public boolean sua(String ma, SanPham spMoi) {
        if (spMoi == null || ma == null || !ma.equalsIgnoreCase(spMoi.getMa())) return false;
        for (int i = 0; i < soSanPham; i++) {
            if (sanPhams[i].getMa().equalsIgnoreCase(ma)) {
                sanPhams[i] = spMoi;
                return true;
            }
        }
        return false;
    }

    @Override public boolean xoa(String ma) {
        if (ma == null) return false;
        for (int i = 0; i < soSanPham; i++) {
            if (sanPhams[i].getMa().equalsIgnoreCase(ma)) {
                System.arraycopy(sanPhams, i + 1, sanPhams, i, soSanPham - i - 1);
                sanPhams[--soSanPham] = null;
                return true;
            }
        }
        return false;
    }

    @Override public SanPham timTheoMa(String ma) {
        if (ma == null) return null;
        for (int i = 0; i < soSanPham; i++) {
            if (sanPhams[i].getMa().equalsIgnoreCase(ma)) return sanPhams[i];
        }
        return null;
    }

    @Override public SanPham[] layDanhSach() {
        return Arrays.copyOf(sanPhams, soSanPham);
    }

    public SanPham[] timTheoTenGanDung(String tuKhoa) {
        if (tuKhoa == null) return new SanPham[0];
        String canTim = tuKhoa.toLowerCase();
        SanPham[] ketQua = new SanPham[soSanPham];
        int n = 0;
        for (int i = 0; i < soSanPham; i++) {
            if (sanPhams[i].getTen().toLowerCase().contains(canTim))
                ketQua[n++] = sanPhams[i];
        }
        return Arrays.copyOf(ketQua, n);
    }

    public void nhapKho(String ma, int soLuong) {
        SanPham sp = timTheoMa(ma);
        if (sp == null) throw new IllegalArgumentException("Không có sản phẩm mã " + ma);
        sp.nhapKho(soLuong);
    }

    // ---- Tài khoản + phân quyền ----
    public boolean themTaiKhoan(TaiKhoan tk) {
        if (tk == null || timTaiKhoan(tk.getTenDangNhap()) != null) return false;
        if (soTaiKhoan == taiKhoans.length) taiKhoans = Arrays.copyOf(taiKhoans, taiKhoans.length * 2);
        taiKhoans[soTaiKhoan++] = tk;
        return true;
    }

    public TaiKhoan timTaiKhoan(String tenDangNhap) {
        if (tenDangNhap == null) return null;
        for (int i = 0; i < soTaiKhoan; i++)
            if (taiKhoans[i].getTenDangNhap().equals(tenDangNhap)) return taiKhoans[i];
        return null;
    }

    public TaiKhoan dangNhap(String tenDangNhap, String matKhau) {
        TaiKhoan tk = timTaiKhoan(tenDangNhap);
        return tk != null && tk.kiemTraMatKhau(matKhau) ? tk : null;
    }

    public TaiKhoan[] layTaiKhoan() { return Arrays.copyOf(taiKhoans, soTaiKhoan); }
    public HoaDon[] layHoaDon() { return Arrays.copyOf(hoaDons, soHoaDon); }

    private void themHoaDon(HoaDon hd) {
        if (soHoaDon == hoaDons.length) hoaDons = Arrays.copyOf(hoaDons, hoaDons.length * 2);
        hoaDons[soHoaDon++] = hd;
    }

    /** Kiểm tra toàn bộ giỏ hàng TRƯỚC khi trừ kho để không bán dở dang. */
    public HoaDon banHang(TaiKhoan nguoiBan, Map<String, Integer> gioHang) {
        if (nguoiBan == null ||
                (nguoiBan.getQuyen() != Quyen.BAN_HANG && nguoiBan.getQuyen() != Quyen.QUAN_LY))
            throw new IllegalArgumentException("Tài khoản không có quyền bán hàng");
        if (gioHang == null || gioHang.isEmpty())
            throw new IllegalArgumentException("Giỏ hàng đang trống");

        Map<SanPham, Integer> danhSachHopLe = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> item : gioHang.entrySet()) {
            SanPham sp = timTheoMa(item.getKey());
            Integer soLuong = item.getValue();
            if (sp == null) throw new IllegalArgumentException("Không tìm thấy: " + item.getKey());
            if (soLuong == null || soLuong <= 0 || soLuong > sp.getTonKho())
                throw new IllegalArgumentException("Không đủ tồn kho cho: " + sp.getMa());
            // Gộp các mã giống nhau (kể cả khác hoa/thường) để tránh trừ kho âm.
            int tong = Math.addExact(danhSachHopLe.getOrDefault(sp, 0), soLuong);
            if (tong > sp.getTonKho())
                throw new IllegalArgumentException("Không đủ tồn kho cho: " + sp.getMa());
            danhSachHopLe.put(sp, tong);
        }

        HoaDon hd = new HoaDon(TienIch.taoMaHoaDon(soHoaDon + 1),
                nguoiBan.getTenDangNhap(), LocalDateTime.now());
        for (Map.Entry<SanPham, Integer> item : danhSachHopLe.entrySet()) {
            SanPham sp = item.getKey();
            int soLuong = item.getValue();
            hd.themChiTiet(new ChiTietHoaDon(sp.getMa(), sp.getTen(),
                    soLuong, sp.tinhDonGia(soLuong))); // đa hình theo loại sản phẩm
        }
        for (Map.Entry<SanPham, Integer> item : danhSachHopLe.entrySet())
            item.getKey().xuatKho(item.getValue());
        themHoaDon(hd);
        return hd;
    }

    // ---- Interface IThongKe ----
    @Override public long tinhDoanhThu() {
        long tong = 0L;
        for (int i = 0; i < soHoaDon; i++) tong = Math.addExact(tong, hoaDons[i].tinhTongTien());
        return tong;
    }

    public long doanhThuTheoNgay(LocalDate ngay) {
        if (ngay == null) throw new IllegalArgumentException("Ngày không được trống");
        long tong = 0;
        for (HoaDon hd : layHoaDon())
            if (hd.getNgayLap().toLocalDate().equals(ngay))
                tong = Math.addExact(tong, hd.tinhTongTien());
        return tong;
    }

    public long doanhThuTheoNhanVien(String tenDangNhap) {
        if (tenDangNhap == null) throw new IllegalArgumentException("Tên nhân viên rỗng");
        long tong = 0;
        for (HoaDon hd : layHoaDon())
            if (hd.getNguoiBan().equals(tenDangNhap))
                tong = Math.addExact(tong, hd.tinhTongTien());
        return tong;
    }

    public int tongSoLuongDaBan(String maSanPham) {
        if (maSanPham == null) throw new IllegalArgumentException("Thiếu mã sản phẩm");
        int tong = 0;
        for (HoaDon hd : layHoaDon())
            for (ChiTietHoaDon ct : hd.layChiTiet())
                if (ct.getMaSanPham().equalsIgnoreCase(maSanPham))
                    tong = Math.addExact(tong, ct.getSoLuong());
        return tong;
    }

    @Override public int tongSoLuongTon() {
        int tong = 0;
        for (int i = 0; i < soSanPham; i++) tong = Math.addExact(tong, sanPhams[i].getTonKho());
        return tong;
    }

    @Override public void hienThiThongKe() {
        System.out.println("\n=== THỐNG KÊ: " + ten + " ===");
        System.out.println("Số loại sản phẩm: " + soSanPham);
        System.out.println("Tổng tồn kho     : " + tongSoLuongTon());
        System.out.println("Số hóa đơn       : " + soHoaDon);
        System.out.println("Doanh thu        : " + TienIch.dinhDangTien(tinhDoanhThu()));
    }

    public void inDanhSachSanPham(SanPham[] ds) {
        if (ds.length == 0) {
            System.out.println("(Không có sản phẩm nào)");
        } else {
            for (SanPham sp : ds) {
                System.out.println(sp);
            }
        }
    }

    // ---- Interface ILuuFile, văn bản UTF-8 ----
    @Override public void ghiFile() throws IOException {
        Files.createDirectories(thuMucDuLieu);
        List<String> dsSP = new ArrayList<>();
        for (SanPham sp : layDanhSach()) {
            int giam = sp instanceof SanPhamKhuyenMai
                    ? ((SanPhamKhuyenMai) sp).getPhanTramGiam() : 0;
            dsSP.add(String.join("|", sp.getLoai(),
                    TienIch.maHoaVanBan(sp.getMa()), TienIch.maHoaVanBan(sp.getTen()),
                    String.valueOf(sp.getGiaGoc()), String.valueOf(sp.getTonKho()), String.valueOf(giam)));
        }
        Files.write(thuMucDuLieu.resolve("sanpham.txt"), dsSP, StandardCharsets.UTF_8);

        List<String> dsTK = new ArrayList<>();
        for (TaiKhoan tk : layTaiKhoan()) {
            dsTK.add(String.join("|", TienIch.maHoaVanBan(tk.getTenDangNhap()),
                    tk.getMatKhauBam(), tk.getQuyen().name()));
        }
        Files.write(thuMucDuLieu.resolve("taikhoan.txt"), dsTK, StandardCharsets.UTF_8);

        List<String> dsHD = new ArrayList<>();
        for (HoaDon hd : layHoaDon()) {
            dsHD.add("HD|" + TienIch.maHoaVanBan(hd.getMa()) + "|" +
                    TienIch.maHoaVanBan(hd.getNguoiBan()) + "|" + hd.getNgayLap());
            for (ChiTietHoaDon ct : hd.layChiTiet()) {
                dsHD.add("CT|" + TienIch.maHoaVanBan(hd.getMa()) + "|" +
                        TienIch.maHoaVanBan(ct.getMaSanPham()) + "|" +
                        TienIch.maHoaVanBan(ct.getTenSanPham()) + "|" + ct.getSoLuong() + "|" +
                        ct.getDonGia());
            }
            dsHD.add("END");
        }
        Files.write(thuMucDuLieu.resolve("hoadon.txt"), dsHD, StandardCharsets.UTF_8);
    }

    @Override public void docFile() throws IOException {
        // Tải vào biến tạm. Nếu 1 file lỗi, dữ liệu hiện tại không bị thay đổi.
        CuaHang tam = new CuaHang(ten, thuMucDuLieu);
        Path fileSP = thuMucDuLieu.resolve("sanpham.txt");
        Path fileTK = thuMucDuLieu.resolve("taikhoan.txt");
        Path fileHD = thuMucDuLieu.resolve("hoadon.txt");
        try {
            if (Files.exists(fileSP)) {
                for (String line : Files.readAllLines(fileSP, StandardCharsets.UTF_8)) {
                    if (line.isBlank()) continue;
                    String[] v = line.split("\\|", -1);
                    if (v.length != 6) throw new IllegalArgumentException("Dòng sản phẩm sai định dạng");
                    String ma = TienIch.giaiMaVanBan(v[1]);
                    String tenSP = TienIch.giaiMaVanBan(v[2]);
                    long gia = Long.parseLong(v[3]);
                    int ton = Integer.parseInt(v[4]);
                    int giam = Integer.parseInt(v[5]);
                    SanPham sp;
                    switch (v[0]) {
                        case "THUONG": sp = new SanPhamThuong(ma, tenSP, gia, ton); break;
                        case "KHUYEN_MAI": sp = new SanPhamKhuyenMai(ma, tenSP, gia, ton, giam); break;
                        case "GIA_SI": sp = new SanPhamGiaSi(ma, tenSP, gia, ton); break;
                        default: throw new IllegalArgumentException("Loại sản phẩm lạ: " + v[0]);
                    }
                    if (!tam.them(sp)) throw new IllegalArgumentException("Trùng mã sản phẩm " + ma);
                }
            }
            if (Files.exists(fileTK)) {
                for (String line : Files.readAllLines(fileTK, StandardCharsets.UTF_8)) {
                    if (line.isBlank()) continue;
                    String[] v = line.split("\\|", -1);
                    if (v.length != 3) throw new IllegalArgumentException("Dòng tài khoản sai định dạng");
                    TaiKhoan tk = TaiKhoan.tuFile(TienIch.giaiMaVanBan(v[0]),
                            v[1], Quyen.valueOf(v[2]));
                    if (!tam.themTaiKhoan(tk)) throw new IllegalArgumentException("Trùng tài khoản");
                }
            }
            if (Files.exists(fileHD)) {
                HoaDon hd = null;
                for (String line : Files.readAllLines(fileHD, StandardCharsets.UTF_8)) {
                    if (line.isBlank()) continue;
                    String[] v = line.split("\\|", -1);
                    switch (v[0]) {
                        case "HD":
                            if (v.length != 4 || hd != null) throw new IllegalArgumentException("Sai đầu hóa đơn");
                            hd = new HoaDon(TienIch.giaiMaVanBan(v[1]),
                                    TienIch.giaiMaVanBan(v[2]), LocalDateTime.parse(v[3]));
                            break;
                        case "CT":
                            if (v.length != 6 || hd == null ||
                                    !hd.getMa().equals(TienIch.giaiMaVanBan(v[1])))
                                throw new IllegalArgumentException("Chi tiết hóa đơn không khớp");
                            hd.themChiTiet(new ChiTietHoaDon(TienIch.giaiMaVanBan(v[2]),
                                    TienIch.giaiMaVanBan(v[3]), Integer.parseInt(v[4]), Long.parseLong(v[5])));
                            break;
                        case "END":
                            if (v.length != 1 || hd == null || hd.layChiTiet().length == 0)
                                throw new IllegalArgumentException("Kết thúc hóa đơn không hợp lệ");
                            tam.themHoaDon(hd);
                            hd = null;
                            break;
                        default: throw new IllegalArgumentException("Dòng hóa đơn lạ");
                    }
                }
                if (hd != null) throw new IllegalArgumentException("Thiếu END ở cuối hóa đơn");
            }
        } catch (RuntimeException ex) {
            throw new IOException("Lỗi định dạng dữ liệu tệp: " + ex.getMessage(), ex);
        }
        sanPhams = tam.sanPhams;
        soSanPham = tam.soSanPham;
        taiKhoans = tam.taiKhoans;
        soTaiKhoan = tam.soTaiKhoan;
        hoaDons = tam.hoaDons;
        soHoaDon = tam.soHoaDon;
    }
}
