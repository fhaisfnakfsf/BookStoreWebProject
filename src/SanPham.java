/** Lớp cơ sở nghiệp vụ mẫu; nhóm có thể thay bằng phân cấp sản phẩm của mình. */
public abstract class SanPham {
    private final String ma;
    private String ten;
    private long giaGoc;
    private int tonKho;

    protected SanPham(String ma, String ten, long giaGoc, int tonKho) {
        if (ma == null || !ma.matches("[A-Za-z0-9_-]{2,20}"))
            throw new IllegalArgumentException("Mã sản phẩm không hợp lệ");
        setTen(ten);
        setGiaGoc(giaGoc);
        if (tonKho < 0 || tonKho > 1_000_000)
            throw new IllegalArgumentException("Tồn kho phải từ 0 đến 1.000.000");
        this.ma = ma.toUpperCase();
        this.tonKho = tonKho;
    }

    public abstract String getLoai();
    public abstract long tinhDonGia(int soLuongMua);  // ĐA HÌNH

    public String getMa() { return ma; }
    public String getTen() { return ten; }
    public long getGiaGoc() { return giaGoc; }
    public int getTonKho() { return tonKho; }

    public void setTen(String ten) {
        if (ten == null || ten.trim().length() < 2 || ten.trim().length() > 100)
            throw new IllegalArgumentException("Tên sản phẩm phải từ 2 đến 100 ký tự");
        this.ten = ten.trim();
    }

    public void setGiaGoc(long giaGoc) {
        if (giaGoc <= 0 || giaGoc > 1_000_000_000L)
            throw new IllegalArgumentException("Giá bán phải từ 1 đến 1 tỷ đồng");
        this.giaGoc = giaGoc;
    }

    public void nhapKho(int soLuong) {
        if (soLuong <= 0 || (long) tonKho + soLuong > 1_000_000L)
            throw new IllegalArgumentException("Số lượng nhập không hợp lệ");
        tonKho += soLuong;
    }

    public void xuatKho(int soLuong) {
        if (soLuong <= 0 || soLuong > tonKho)
            throw new IllegalArgumentException("Số lượng xuất vượt tồn kho");
        tonKho -= soLuong;
    }

    @Override public String toString() {
        return String.format("%-9s | %-27s | %-12s | %13s | tồn: %5d",
                ma, ten, getLoai(), TienIch.dinhDangTien(giaGoc), tonKho);
    }
}
