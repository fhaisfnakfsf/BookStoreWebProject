import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/** Các tiện ích static: định dạng, mã hóa chuỗi để lưu file, sinh mã. */
public final class TienIch {
    private static final DateTimeFormatter NGAY_GIO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private TienIch() { }

    public static String dinhDangTien(long dong) {
        return String.format("%,d đ", dong).replace(',', '.');
    }

    public static String dinhDangNgayGio(LocalDateTime ngay) {
        return ngay.format(NGAY_GIO);
    }

    public static String maHoaVanBan(String giaTri) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(giaTri.getBytes(StandardCharsets.UTF_8));
    }

    public static String giaiMaVanBan(String giaTri) {
        return new String(Base64.getUrlDecoder().decode(giaTri), StandardCharsets.UTF_8);
    }

    public static String taoMaHoaDon(int soThuTu) {
        return String.format("HD%06d", soThuTu);
    }
}
