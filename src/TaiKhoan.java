import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class TaiKhoan {
    private final String tenDangNhap;
    private String matKhauBam;
    private Quyen quyen;

    // Constructor thông thường: nhận mật khẩu để đăng nhập.
    public TaiKhoan(String tenDangNhap, String matKhau, Quyen quyen) {
        kiemTraTen(tenDangNhap);
        if (matKhau == null || matKhau.length() < 6) {
            throw new IllegalArgumentException("Mật khẩu phải có tối thiểu 6 ký tự");
        }
        if (quyen == null) throw new IllegalArgumentException("Chưa chọn quyền");
        this.tenDangNhap = tenDangNhap;
        this.matKhauBam = bam(tenDangNhap, matKhau);
        this.quyen = quyen;
    }

    private TaiKhoan(String tenDangNhap, String matKhauBam, Quyen quyen, boolean tuFile) {
        kiemTraTen(tenDangNhap);
        if (!matKhauBam.matches("[0-9a-f]{64}") || quyen == null) {
            throw new IllegalArgumentException("Tệp tài khoản không hợp lệ");
        }
        this.tenDangNhap = tenDangNhap;
        this.matKhauBam = matKhauBam;
        this.quyen = quyen;
    }

    public static TaiKhoan tuFile(String ten, String matKhauBam, Quyen quyen) {
        return new TaiKhoan(ten, matKhauBam, quyen, true);
    }

    private static void kiemTraTen(String ten) {
        if (ten == null || !ten.matches("[A-Za-z0-9_]{3,24}")) {
            throw new IllegalArgumentException("Tên đăng nhập không hợp lệ");
        }
    }

    // Dùng SHA-256 kèm tên đăng nhập cho đồ án học tập; hệ thống thực tế nên dùng Argon2/PBKDF2.
    private static String bam(String ten, String matKhau) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest((ten + ":" + matKhau)
                    .getBytes(StandardCharsets.UTF_8));
            StringBuilder b = new StringBuilder();
            for (byte x : bytes) b.append(String.format("%02x", x & 0xff));
            return b.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Java thiếu SHA-256", e);
        }
    }

    public boolean kiemTraMatKhau(String matKhau) {
        if (matKhau == null) return false;
        return MessageDigest.isEqual(
                matKhauBam.getBytes(StandardCharsets.US_ASCII),
                bam(tenDangNhap, matKhau).getBytes(StandardCharsets.US_ASCII));
    }

    public void doiMatKhau(String matKhauMoi) {
        if (matKhauMoi == null || matKhauMoi.length() < 6)
            throw new IllegalArgumentException("Mật khẩu tối thiểu 6 ký tự");
        this.matKhauBam = bam(tenDangNhap, matKhauMoi);
    }

    public String getTenDangNhap() { return tenDangNhap; }
    public String getMatKhauBam() { return matKhauBam; }
    public Quyen getQuyen() { return quyen; }
    public void setQuyen(Quyen quyen) {
        if (quyen == null) throw new IllegalArgumentException("Thiếu quyền");
        this.quyen = quyen;
    }

    @Override public String toString() {
        return tenDangNhap + " (" + quyen.getTen() + ")";
    }
}
