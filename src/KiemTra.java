import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/** Mọi thao tác nhập đều dùng nextLine() để tránh lỗi trôi dòng. */
public final class KiemTra {
    private static final Pattern MA = Pattern.compile("[A-Za-z0-9_-]{2,20}");
    private static final Pattern USERNAME = Pattern.compile("[A-Za-z0-9_]{3,24}");
    private static final DateTimeFormatter DINH_DANG_NGAY =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private KiemTra() { }

    private static String docDong(Scanner sc, String nhac) {
        System.out.print(nhac);
        if (!sc.hasNextLine()) {
            throw new NoSuchElementException("Đã hết dữ liệu nhập.");
        }
        return sc.nextLine().trim();
    }

    public static String nhapChuoi(Scanner sc, String nhac, int min, int max) {
        while (true) {
            String s = docDong(sc, nhac);
            if (s.length() >= min && s.length() <= max && !s.contains("\t")) {
                return s;
            }
            System.out.printf("[LỖI] Nội dung phải dài %d-%d ký tự và không chứa tab.%n", min, max);
        }
    }

    public static String nhapMa(Scanner sc, String nhac) {
        while (true) {
            String s = docDong(sc, nhac).toUpperCase();
            if (MA.matcher(s).matches()) return s;
            System.out.println("[LỖI] Mã dài 2-20 ký tự, chỉ gồm chữ không dấu, số, _ hoặc -.");
        }
    }

    public static String nhapTenDangNhap(Scanner sc, String nhac) {
        while (true) {
            String s = docDong(sc, nhac);
            if (USERNAME.matcher(s).matches()) return s;
            System.out.println("[LỖI] Tên đăng nhập dài 3-24 ký tự, chỉ gồm chữ không dấu, số, _.");
        }
    }

    public static String nhapMatKhau(Scanner sc, String nhac) {
        while (true) {
            String s = docDong(sc, nhac);
            if (s.length() >= 6 && s.length() <= 60 && !s.contains("\t")) return s;
            System.out.println("[LỖI] Mật khẩu phải dài 6-60 ký tự.");
        }
    }

    public static int nhapSoNguyen(Scanner sc, String nhac, int min, int max) {
        if (min > max) throw new IllegalArgumentException("Khoảng không hợp lệ");
        while (true) {
            String s = docDong(sc, nhac);
            try {
                int v = Integer.parseInt(s);
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) { }
            System.out.printf("[LỖI] Nhập số nguyên từ %d đến %d.%n", min, max);
        }
    }

    public static long nhapSoTien(Scanner sc, String nhac, long min, long max) {
        if (min > max) throw new IllegalArgumentException("Khoảng không hợp lệ");
        while (true) {
            String s = docDong(sc, nhac);
            try {
                // Chỉ chấp nhận số nguyên (VND), không chấp nhận 12.5 hoặc 1e3.
                if (!s.matches("[0-9]+")) throw new NumberFormatException();
                long v = Long.parseLong(s);
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) { }
            System.out.printf("[LỖI] Số tiền phải là số nguyên từ %d đến %d VND.%n", min, max);
        }
    }

    public static double nhapSoThuc(Scanner sc, String nhac, double min, double max) {
        if (!Double.isFinite(min) || !Double.isFinite(max) || min > max)
            throw new IllegalArgumentException("Khoảng không hợp lệ");
        while (true) {
            String s = docDong(sc, nhac);
            try {
                // Ví dụ 2.5; loại bỏ NaN, Infinity, khoảng trắng và biểu diễn không chuẩn.
                if (!s.matches("[+-]?[0-9]+(\\.[0-9]+)?")) throw new NumberFormatException();
                double v = Double.parseDouble(s);
                if (Double.isFinite(v) && v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) { }
            System.out.printf("[LỖI] Nhập số thực từ %s đến %s (dùng dấu chấm).%n", min, max);
        }
    }

    public static LocalDate nhapNgay(Scanner sc, String nhac) {
        while (true) {
            String s = docDong(sc, nhac + " (dd/MM/yyyy): ");
            try {
                return LocalDate.parse(s, DINH_DANG_NGAY);
            } catch (DateTimeParseException ignored) {
                System.out.println("[LỖI] Ngày không hợp lệ. Ví dụ: 28/02/2026.");
            }
        }
    }

    public static boolean xacNhan(Scanner sc, String nhac) {
        while (true) {
            String s = docDong(sc, nhac + " (c/k): ").toLowerCase();
            if (s.equals("c") || s.equals("co") || s.equals("có")) return true;
            if (s.equals("k") || s.equals("khong") || s.equals("không")) return false;
            System.out.println("[LỖI] Chỉ nhập c hoặc k.");
        }
    }
}
