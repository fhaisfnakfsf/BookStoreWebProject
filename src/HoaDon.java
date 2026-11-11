import java.time.LocalDateTime;
import java.util.Arrays;

public final class HoaDon {
    private final String ma;
    private final String nguoiBan;
    private final LocalDateTime ngayLap;
    private ChiTietHoaDon[] chiTiet = new ChiTietHoaDon[4];
    private int soChiTiet = 0;

    public HoaDon(String ma, String nguoiBan, LocalDateTime ngayLap) {
        if (ma == null || ma.isBlank() || nguoiBan == null || nguoiBan.isBlank() || ngayLap == null)
            throw new IllegalArgumentException("Thiếu thông tin hóa đơn");
        this.ma = ma;
        this.nguoiBan = nguoiBan;
        this.ngayLap = ngayLap;
    }

    public void themChiTiet(ChiTietHoaDon ct) {
        if (ct == null) throw new IllegalArgumentException("Chi tiết rỗng");
        if (soChiTiet == chiTiet.length)
            chiTiet = Arrays.copyOf(chiTiet, chiTiet.length * 2);
        // Tính trước để từ chối nếu tổng tiền bị tràn.
        Math.addExact(tinhTongTien(), ct.tinhThanhTien());
        chiTiet[soChiTiet++] = ct;
    }

    public long tinhTongTien() {
        long tong = 0;
        for (int i = 0; i < soChiTiet; i++)
            tong = Math.addExact(tong, chiTiet[i].tinhThanhTien());
        return tong;
    }

    public ChiTietHoaDon[] layChiTiet() {
        return Arrays.copyOf(chiTiet, soChiTiet);
    }

    public String getMa() { return ma; }
    public String getNguoiBan() { return nguoiBan; }
    public LocalDateTime getNgayLap() { return ngayLap; }

    public void inHoaDon() {
        System.out.printf("%n===== HÓA ĐƠN %s =====%n", ma);
        System.out.println("Nhân viên: " + nguoiBan);
        System.out.println("Ngày lập : " + TienIch.dinhDangNgayGio(ngayLap));
        for (ChiTietHoaDon ct : layChiTiet()) {
            System.out.printf("%-9s %-25s x%-4d %14s%n",
                    ct.getMaSanPham(), ct.getTenSanPham(), ct.getSoLuong(),
                    TienIch.dinhDangTien(ct.tinhThanhTien()));
        }
        System.out.println("TỔNG TIỀN: " + TienIch.dinhDangTien(tinhTongTien()));
    }
}
